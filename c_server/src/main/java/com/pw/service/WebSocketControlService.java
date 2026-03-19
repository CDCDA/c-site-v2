package com.pw.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.config.RabbitMQConfig;
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
     * @param data 消息数据
     */
    public void sendBroadcastMessage(String channel, Object data) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "broadcast");
        message.put("channel", channel);
        message.put("data", data);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到广播交换机
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE, 
                             RabbitMQConfig.WEBSOCKET_BROADCAST_ROUTING_KEY);
    }

    /**
     * 发送消息给特定用户（通过用户独立队列）
     * 
     * @param userId 用户 ID
     * @param messageData 消息内容
     */
    public void sendUserMessage(Long userId, Object messageData) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "user_message");
        message.put("userId", userId);
        message.put("message", messageData);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到用户队列
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, userRoutingKey);
    }

    /**
     * 发送系统通知（广播到系统通知频道）
     * 
     * @param title 通知标题
     * @param content 通知内容
     * @param status 通知状态（primary/warning/error）
     */
    public void sendSystemNotice(String title, String content, String status) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "system_notice");
        message.put("title", title);
        message.put("content", content);
        message.put("status", status);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到广播交换机 - 系统通知频道
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE,
                             "broadcast.system_notice");
    }

    /**
     * 发送磁盘信息更新（广播）
     * 
     * @param diskInfo 磁盘信息数据
     */
    public void sendDiskInfo(Object diskInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "disk_info");
        message.put("data", diskInfo);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到广播交换机 - 磁盘信息频道
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE,
                             "broadcast.disk_info");
    }

    /**
     * 发送待办事项通知（发送给特定用户）
     * 
     * @param userId 用户 ID
     * @param todoInfo 待办事项数据
     */
    public void sendTodoNotification(Long userId, Object todoInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "todo_notification");
        message.put("userId", userId);
        message.put("data", todoInfo);
        message.put("timestamp", System.currentTimeMillis());

        // 发送到用户队列
        String userRoutingKey = RabbitMQConfig.getUserRoutingKey(userId);
        sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE, userRoutingKey);
    }

    /**
     * 发送通用控制消息
     * 
     * @param type 消息类型
     * @param payload 消息负载
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
        } else {
            // 广播
            sendMessageToRabbitMQ(message, RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE,
                                 RabbitMQConfig.WEBSOCKET_BROADCAST_ROUTING_KEY);
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
     */
    private void sendMessageToRabbitMQ(Map<String, Object> message, String exchange, String routingKey) {
        try {
            log.info("📤 发送控制消息到 RabbitMQ - 交换机：{}, 路由键：{}, 类型：{}", 
                    exchange, routingKey, message.get("type"));
            
            rabbitTemplate.convertAndSend(exchange, routingKey, message);
            
            log.info("✅ 控制消息发送成功");
            
        } catch (Exception e) {
            log.error("❌ 发送控制消息失败", e);
            throw new RuntimeException("发送控制消息失败", e);
        }
    }
}
