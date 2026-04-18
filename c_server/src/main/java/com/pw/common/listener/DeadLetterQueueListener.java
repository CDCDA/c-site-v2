package com.pw.common.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

/**
 * 死信队列监听器
 * 处理无法正常消费的 WebSocket 消息
 *
 * @author cyd
 * @create 2026/04/11
 */
@Component
@Slf4j
public class DeadLetterQueueListener implements ChannelAwareMessageListener {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
        try {
            String messageBody = new String(message.getBody(), "UTF-8");

            // 获取消息属性
            String exchange = message.getMessageProperties().getReceivedExchange();
            String routingKey = message.getMessageProperties().getReceivedRoutingKey();
            String queue = message.getMessageProperties().getConsumerQueue();

            log.error("💀 死信队列收到失败消息");
            log.error("   消息内容: {}", messageBody);
            log.error("   来源交换机: {}", exchange);
            log.error("   路由键: {}", routingKey);
            log.error("   死信队列: {}", queue);

            // 解析消息内容
            try {
                JsonNode jsonNode = objectMapper.readTree(messageBody);

                // 记录失败原因（如果消息中包含）
                if (jsonNode.has("errorReason")) {
                    log.error("   失败原因: {}", jsonNode.get("errorReason").asText());
                }

                // 记录原始消息类型
                if (jsonNode.has("type")) {
                    String messageType = jsonNode.get("type").asText();
                    log.error("   原始消息类型: {}", messageType);

                    // 根据消息类型采取不同的处理策略
                    switch (messageType) {
                        case "broadcast":
                            handleFailedBroadcastMessage(jsonNode);
                            break;
                        case "user_message":
                            handleFailedUserMessage(jsonNode);
                            break;
                        case "system_notice":
                            handleFailedSystemNotice(jsonNode);
                            break;
                        default:
                            handleFailedGenericMessage(jsonNode);
                    }
                }
            } catch (Exception e) {
                log.warn("消息内容不是有效的 JSON 格式，将作为文本处理");
            }

            // 记录消息的其他属性
            log.error("   消息ID: {}", message.getMessageProperties().getMessageId());
            log.error("   优先级: {}", message.getMessageProperties().getPriority());
            log.error("   过期时间: {}", message.getMessageProperties().getExpiration());

            // 手动 ACK 确认消息已处理
            // 注意：死信队列的消息通常需要人工介入或特殊处理，这里只是记录日志
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            log.info("✅ 死信消息已确认处理");

        } catch (Exception e) {
            log.error("❌ 处理死信队列消息时发生异常", e);
            try {
                // 处理失败，拒绝消息，不再重新入队
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
            } catch (Exception ex) {
                log.error("死信消息确认失败", ex);
            }
        }
    }

    /**
     * 处理失败的广播消息
     */
    private void handleFailedBroadcastMessage(JsonNode jsonNode) {
        String targetChannel = jsonNode.has("channel") ? jsonNode.get("channel").asText() : "未知频道";
        log.error("⚠️ 广播消息发送失败，目标频道: {}", targetChannel);

        // 可以在这里实现以下逻辑：
        // 1. 记录到数据库供后续分析
        // 2. 尝试通过备用通道重试
        // 3. 发送告警通知
    }

    /**
     * 处理失败的用户消息
     */
    private void handleFailedUserMessage(JsonNode jsonNode) {
        Long userId = jsonNode.has("userId") ? jsonNode.get("userId").asLong() : null;
        log.error("⚠️ 用户消息发送失败，用户ID: {}", userId);

        // 可以在这里实现以下逻辑：
        // 1. 记录到数据库供后续重试
        // 2. 如果用户在线，尝试通过其他方式通知
        // 3. 发送系统告警
    }

    /**
     * 处理失败的系统通知
     */
    private void handleFailedSystemNotice(JsonNode jsonNode) {
        String title = jsonNode.has("title") ? jsonNode.get("title").asText() : "未知通知";
        log.error("⚠️ 系统通知发送失败，标题: {}", title);

        // 系统通知失败可能比较重要，建议：
        // 1. 立即发送告警通知管理员
        // 2. 记录到数据库
        // 3. 考虑通过邮件、短信等备用渠道发送
    }

    /**
     * 处理失败的通用消息
     */
    private void handleFailedGenericMessage(JsonNode jsonNode) {
        log.error("⚠️ 未知类型的消息发送失败");
        log.error("   完整消息: {}", jsonNode.toString());
    }
}
