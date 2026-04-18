package com.pw.common.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.config.RabbitMQConfig;
import com.pw.domain.RetryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 重试消息监听器
 * 监听重试队列，处理延迟重试逻辑
 *
 * @author cyd
 * @create 2026/04/11
 */
@Component
@Slf4j
public class RetryMessageListener {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 处理重试队列中的消息
     * <p>
     * 流程：
     * 1. 解析 RetryMessage
     * 2. 判断重试次数
     * 3. 重试发送到目标交换机
     * 4. 成功则 ACK，失败则放入下一个延迟队列或最终死信队列
     */
    public void handleMessage(Message message) {
        try {
            // 解析 RetryMessage
            RetryMessage retryMessage = (RetryMessage) messageConverter.fromMessage(message);
            int currentRetryCount = retryMessage.getRetryCount();

            log.info("🔄 收到重试消息 - 原始ID: {}, 当前重试次数: {}/{}, 目标交换机: {}, 目标路由键: {}",
                    retryMessage.getOriginalMessageId(),
                    currentRetryCount + 1,
                    RetryMessage.MAX_RETRY_COUNT,
                    retryMessage.getTargetExchange(),
                    retryMessage.getTargetRoutingKey());

            // 检查是否达到最大重试次数
            if (retryMessage.isMaxRetryReached()) {
                // 达到最大重试次数，发送到最终死信队列
                log.warn("⚠️ 达到最大重试次数(8次)，消息发送到最终死信队列 - 原始ID: {}, 目标交换机: {}",
                        retryMessage.getOriginalMessageId(), retryMessage.getTargetExchange());

                rabbitTemplate.convertAndSend(
                        RabbitMQConfig.WEBSOCKET_FINAL_DLX,
                        RabbitMQConfig.WEBSOCKET_FINAL_DLQ_ROUTING_KEY,
                        retryMessage
                );
            } else {
                // 未达到最大重试次数，尝试重新发送
                boolean sendSuccess = sendToTarget(retryMessage);

                if (sendSuccess) {
                    // 发送成功，记录日志
                    long totalRetryTime = System.currentTimeMillis() - retryMessage.getFirstFailTime();
                    log.info("✅ 重试发送成功 - 原始ID: {}, 重试次数: {}, 总耗时: {}ms",
                            retryMessage.getOriginalMessageId(), currentRetryCount + 1, totalRetryTime);
                } else {
                    // 发送失败，创建下一次重试并放入下一个延迟队列
                    handleRetryFailure(retryMessage);
                }
            }
        } catch (Exception e) {
            log.error("❌ 处理重试消息时发生异常", e);
            throw new RuntimeException("处理重试消息失败", e);
        }
    }

    /**
     * 尝试发送到目标交换机
     */
    private boolean sendToTarget(RetryMessage retryMessage) {
        try {
            log.info("📤 重试发送到目标交换机 - 交换机: {}, 路由键: {}, 消息类型: {}, 错误原因: {}",
                    retryMessage.getTargetExchange(),
                    retryMessage.getTargetRoutingKey(),
                    retryMessage.getMessage() != null ? retryMessage.getMessage().get("type") : "unknown",
                    retryMessage.getErrorMessage());

            // 发送到目标交换机
            rabbitTemplate.convertAndSend(
                    retryMessage.getTargetExchange(),
                    retryMessage.getTargetRoutingKey(),
                    retryMessage.getMessage()
            );
            return true;
        } catch (Exception e) {
            log.error("❌ 重试发送失败 - 交换机: {}, 路由键: {}, 错误: {}",
                    retryMessage.getTargetExchange(),
                    retryMessage.getTargetRoutingKey(),
                    e.getMessage());

            // 更新错误信息（保留原有错误信息）
            String originalError = retryMessage.getErrorMessage();
            retryMessage.setErrorMessage(originalError + " | 重试失败: " + e.getMessage());
            return false;
        }
    }

    /**
     * 处理重试失败，使用延迟消息插件安排下一次重试
     */
    private void handleRetryFailure(RetryMessage retryMessage) {
        int currentRetryCount = retryMessage.getRetryCount();

        if (currentRetryCount >= RetryMessage.MAX_RETRY_COUNT) {
            // 达到最大重试次数，发送到最终死信队列
            log.warn("⚠️ 达到最大重试次数({}次)，消息发送到最终死信队列 - 原始ID: {}",
                    RetryMessage.MAX_RETRY_COUNT, retryMessage.getOriginalMessageId());

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_FINAL_DLX,
                    RabbitMQConfig.WEBSOCKET_FINAL_DLQ_ROUTING_KEY,
                    retryMessage
            );
        } else {
            // 创建下一次重试消息
            RetryMessage nextRetry = retryMessage.createNextRetry();
            long baseDelay = nextRetry.getCurrentDelay();

            // 🔥 计算带随机波动的延迟时间（±20%）
            long jitter = (long) (baseDelay * 0.2 * (Math.random() - 0.5));
            long actualDelay = Math.max(0, baseDelay + jitter);

            log.warn("⏳ 安排下次重试 - 原始ID: {}, 重试次数: {}/{}, 基础延迟: {}ms, 波动: {}ms, 实际延迟: {}ms",
                    retryMessage.getOriginalMessageId(),
                    nextRetry.getRetryCount() + 1,
                    RetryMessage.MAX_RETRY_COUNT,
                    baseDelay,
                    jitter,
                    actualDelay);

            // 使用延迟消息插件发送（通过消息头设置延迟时间）
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_RETRY_EXCHANGE,
                    RabbitMQConfig.WEBSOCKET_RETRY_ROUTING_KEY,
                    nextRetry,
                    msg -> {
                        msg.getMessageProperties().setDelayLong(actualDelay);
                        return msg;
                    }
            );
        }
    }
}
