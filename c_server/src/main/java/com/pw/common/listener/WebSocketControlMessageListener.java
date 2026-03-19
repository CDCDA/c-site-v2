package com.pw.common.listener;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.common.handler.CustomWebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

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

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
        try {
            String messageBody = new String(message.getBody(), "UTF-8");
            log.info("收到 RabbitMQ 控制消息：{}", messageBody);

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

            // 手动 ACK 确认消息已处理
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        } catch (Exception e) {
            log.error("❌ 处理 RabbitMQ 控制消息失败", e);
            // 处理失败，拒绝消息并重新入队
            try {
                channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
            } catch (Exception ex) {
                log.error("消息拒绝失败", ex);
            }
        }
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
