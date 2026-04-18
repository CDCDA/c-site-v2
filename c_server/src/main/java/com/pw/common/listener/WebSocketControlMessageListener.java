package com.pw.common.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.common.handler.CustomWebSocketHandler;
import com.pw.config.RabbitMQConfig;
import com.pw.domain.RetryMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Thread.sleep;

/**
 * WebSocket 控制总线消息监听器
 * 处理来自 RabbitMQ 的控制消息并转发到 WebSocket
 *
 * @author cyd
 * @create 2026/03/18
 */
@Component
@Slf4j
public class WebSocketControlMessageListener implements ChannelAwareMessageListener {

    @Autowired
    private CustomWebSocketHandler customWebSocketHandler;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 消息处理超时时间（20秒）
     */
    private static final long MESSAGE_TIMEOUT_MS = 20000L;

    @Override
    public void onMessage(Message message, Channel channel) throws Exception {
        long startTime = System.currentTimeMillis();
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            String messageBody = new String(message.getBody(), "UTF-8");
            log.info("收到 RabbitMQ 控制消息：{}", messageBody);

            // 检查是否超时
            checkTimeout(startTime, deliveryTag);

            // 解析消息内容
            JsonNode jsonNode = objectMapper.readTree(messageBody);

            // 提取消息类型和目标频道
            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : "unknown";

            // 根据消息类型处理
            switch (type) {
                case "broadcast":
                    // 广播消息到指定频道
                    String targetChannel = jsonNode.has("channel") ? jsonNode.get("channel").asText() : "";
                    handleBroadcastMessage(jsonNode, targetChannel);
                    break;

                case "user_message":
                    // 发送给特定用户
                    handleUserMessage(jsonNode);
                    break;

                case "system_notice":
                    // 系统通知（广播到系统通知频道）
                    handleSystemNotice(jsonNode);
                    break;

                case "disk_info":
                    // 磁盘信息更新
                    handleDiskInfo(jsonNode);
                    break;

                case "todo_notification":
                    // 待办事项通知
                    handleTodoNotification(jsonNode);
                    break;

                default:
                    log.warn("⚠️ 未知的消息类型：{}", type);
            }
//            sleep(20000);
            // 最终检查是否超时
            checkTimeout(startTime, deliveryTag);

            // 手动 ACK 确认消息已处理
            channel.basicAck(deliveryTag, false);

        } catch (RuntimeException e) {
            handleTimeout(message, channel, deliveryTag, startTime, e.getMessage());
        } catch (Exception e) {
            log.error("❌ 处理 RabbitMQ 控制消息失败", e);
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (Exception ex) {
                log.error("消息拒绝失败", ex);
            }
        }
    }

    /**
     * 检查是否超时
     */
    private void checkTimeout(long startTime, long deliveryTag) throws RuntimeException {
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed > MESSAGE_TIMEOUT_MS) {
            throw new RuntimeException("消息处理超时: " + elapsed + "ms");
        }
    }

    /**
     * 处理超时消息
     */
    private void handleTimeout(Message message, Channel channel, long deliveryTag,
                               long startTime, String reason) throws Exception {
        long elapsed = System.currentTimeMillis() - startTime;
        log.warn("⏰ 消息处理超时 - 耗时: {}ms, 原因: {}", elapsed, reason);

        try {
            // 解析原始消息
            String messageBody = new String(message.getBody(), "UTF-8");
            @SuppressWarnings("unchecked")
            Map<String, Object> originalMessage = objectMapper.readValue(messageBody, Map.class);

            // 创建重试消息，标记为超时
            RetryMessage retryMessage = new RetryMessage();
            retryMessage.setMessage(originalMessage);
            retryMessage.setTargetExchange(RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE);
            retryMessage.setTargetRoutingKey(message.getMessageProperties().getReceivedRoutingKey());
            retryMessage.setRetryCount(0);
            retryMessage.setOriginalMessageId(UUID.randomUUID().toString());
            retryMessage.setFirstFailTime(System.currentTimeMillis());
            retryMessage.setErrorMessage("超时: " + reason);

            // 发送到重试队列（延迟1秒后重试）
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_RETRY_EXCHANGE,
                    RabbitMQConfig.WEBSOCKET_RETRY_ROUTING_KEY,
                    retryMessage,
                    msg -> {
                        msg.getMessageProperties().setDelayLong(1000L); // 1秒后重试
                        return msg;
                    }
            );

            log.info("📤 超时消息已发送到重试队列 - 原始消息ID: {}", retryMessage.getOriginalMessageId());

        } catch (Exception e) {
            log.error("❌ 发送超时消息到重试队列失败", e);
        }

        // 拒绝消息，不重新入队（已经发送到重试队列）
        channel.basicNack(deliveryTag, false, false);
    }

    /**
     * 处理广播消息
     */
    private void handleBroadcastMessage(JsonNode jsonNode, String channel) {
        if (channel == null || channel.isEmpty()) {
            log.warn("广播消息缺少目标频道");
            return;
        }

        Object data = jsonNode.has("data") ? convertJsonNodeToObject(jsonNode.get("data")) : null;

        log.info("📢 向频道 {} 广播消息", channel);
        customWebSocketHandler.sendMessageToChannel(channel, data);
    }

    /**
     * 处理用户消息
     */
    private void handleUserMessage(JsonNode jsonNode) {
        Long userId = jsonNode.has("userId") ? jsonNode.get("userId").asLong() : null;
        if (userId == null) {
            log.warn("用户消息缺少 userId");
            return;
        }
        Object message = jsonNode.has("message") ? convertJsonNodeToObject(jsonNode.get("message")) : null;
        log.info("👤 向用户 {} 发送消息", userId);
        customWebSocketHandler.sendMessageToUser(userId, message);
    }

    /**
     * 处理系统通知
     */
    private void handleSystemNotice(JsonNode jsonNode) {
        Map<String, Object> notice = new HashMap<>();
        notice.put("title", jsonNode.has("title") ? jsonNode.get("title").asText() : "系统通知");
        notice.put("content", jsonNode.has("content") ? jsonNode.get("content").asText() : "");
        notice.put("status", jsonNode.has("status") ? jsonNode.get("status").asText() : "primary");
        notice.put("timestamp", System.currentTimeMillis());

        log.info("🔔 发送系统通知：{}", notice.get("title"));
        customWebSocketHandler.sendSystemNotice(notice);
    }

    /**
     * 处理磁盘信息
     */
    private void handleDiskInfo(JsonNode jsonNode) {
        Map<String, Object> diskInfo = convertJsonNodeToObject(jsonNode);
        log.info("💾 更新磁盘信息");
        customWebSocketHandler.sendDiskInfo(diskInfo);
    }

    /**
     * 处理待办事项通知
     */
    private void handleTodoNotification(JsonNode jsonNode) {
        Map<String, Object> todoInfo = convertJsonNodeToObject(jsonNode);
        log.info("📝 更新待办事项");
        customWebSocketHandler.sendTodoNotification(todoInfo);
    }

    /**
     * 将 JsonNode 转换为 Map
     */
    @SuppressWarnings("unchecked")
    private <T> T convertJsonNodeToObject(JsonNode jsonNode) {
        try {
            return (T) objectMapper.treeToValue(jsonNode, Object.class);
        } catch (Exception e) {
            log.error("JSON 转换失败", e);
            return null;
        }
    }
}
