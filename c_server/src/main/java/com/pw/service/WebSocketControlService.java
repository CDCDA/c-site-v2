package com.pw.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.config.RabbitMQConfig;
import com.pw.domain.RetryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket 控制总线服务
 * 用于通过 RabbitMQ 发送控制消息到 WebSocket
 *
 * @author cyd
 * @create 2026/03/18
 */
@Service
@Slf4j
public class WebSocketControlService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 发送广播消息（所有订阅广播频道的用户都会收到）
     *
     * @param channel 目标频道
     * @param data    消息数据
     */
    public void sendBroadcastMessage(String channel, Object data) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "broadcast");
        message.put("channel", channel);
        message.put("data", data);
        message.put("timestamp", System.currentTimeMillis());
    }

    /**
     * 发送消息给特定用户
     *
     * @param userId      用户 ID
     * @param messageData 消息内容
     */
    public void sendUserMessage(Long userId, Object messageData) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "user_message");
        message.put("userId", userId);
        message.put("message", messageData);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到控制总线交换机，路由键为 user.{userId}
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, userRoutingKey);
    }



    /**
     * 发送待办事项通知（发送给特定用户）
     *
     * @param userId   用户 ID
     * @param todoInfo 待办事项数据
     */
    public void sendTodoNotification(Long userId, Object todoInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "todo_notification");
        message.put("userId", userId);
        message.put("data", todoInfo);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到控制总线交换机，路由键为 user.{userId}
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, userRoutingKey);
    }

    /**
     * 发送通用控制消息
     *
     * @param type         消息类型
     * @param payload      消息负载
     * @param targetUserId 目标用户 ID（null 表示广播）
     */
    public void sendControlMessage(String type, Map<String, Object> payload, Long targetUserId) {
        Map<String, Object> message = new HashMap<>(payload);
        message.put("type", type);
        message.put("timestamp", System.currentTimeMillis());

        if (targetUserId != null) {
            // 发送给特定用户
            String userRoutingKey = RabbitMQConfig.getUserRoutingKey(targetUserId);
            sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, userRoutingKey);
        }
    }

    /**
     * 发送消息到 RabbitMQ
     */
    private void sendMessageToRabbitMQ(Map<String, Object> message) {
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE,
                RabbitMQConfig.WEBSOCKET_CONTROL_ROUTING_KEY);
    }

    /**
     * 发送消息到指定的 RabbitMQ 交换机和路由键
     * 使用非阻塞重试机制（基于延迟消息插件）：
     * - 发送失败时，消息自动进入延迟队列进行重试
     * - 最多重试8次
     * - 重试间隔：1s, 2s, 4s, 8s, 16s, 32s, 64s, 128s（指数增长 + 随机波动）
     * - 完全非阻塞，线程立即释放
     */
    private void sendMessageToRabbitMQ(Map<String, Object> message, String exchange, String routingKey) {
        try {
            log.info("📤 发送控制消息到 RabbitMQ - 交换机：{}, 路由键：{}, 类型：{}",
                    exchange, routingKey, message.get("type"));

            rabbitTemplate.convertAndSend(exchange, routingKey, message);

            log.info("✅ 控制消息发送成功");

        } catch (Exception e) {
            log.error("❌ 发送控制消息失败，将使用非阻塞重试 - 交换机：{}, 路由键：{}, 错误：{}",
                    exchange, routingKey, e.getMessage());

            // 创建重试消息并发送到延迟交换机（第一次重试）
            RetryMessage retryMessage = RetryMessage.createForFirstRetry(
                    message,
                    exchange,
                    routingKey,
                    e.getMessage()
            );

            // 计算第一次重试的延迟时间（1s + 随机波动）
            long baseDelay = retryMessage.getCurrentDelay();
            long jitter = (long) (baseDelay * 0.2 * (Math.random() - 0.5)); // ±20% 随机波动
            long actualDelay = Math.max(0, baseDelay + jitter);

            log.warn("⏳ 消息已放入延迟队列 - 原始ID: {}, 基础延迟: {}ms, 波动: {}ms, 实际延迟: {}ms",
                    retryMessage.getOriginalMessageId(), baseDelay, jitter, actualDelay);

            // 使用延迟消息插件发送（通过消息头设置延迟时间）
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_RETRY_EXCHANGE,
                    RabbitMQConfig.WEBSOCKET_RETRY_ROUTING_KEY,
                    retryMessage,
                    msg -> {
                        msg.getMessageProperties().setDelayLong(actualDelay);
                        return msg;
                    }
            );
        }
    }
}
