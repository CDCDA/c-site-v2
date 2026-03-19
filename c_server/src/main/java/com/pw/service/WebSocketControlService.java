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
     * 发送广播消息到指定频道
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

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送消息给特定用户
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

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送系统通知
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

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送磁盘信息更新
     * 
     * @param diskInfo 磁盘信息数据
     */
    public void sendDiskInfo(Object diskInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "disk_info");
        message.put("data", diskInfo);
        message.put("timestamp", System.currentTimeMillis());

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送待办事项通知
     * 
     * @param todoInfo 待办事项数据
     */
    public void sendTodoNotification(Object todoInfo) {
        Map<String, Object> message = new HashMap<>();
        message.put("type", "todo_notification");
        message.put("data", todoInfo);
        message.put("timestamp", System.currentTimeMillis());

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送通用控制消息
     * 
     * @param type 消息类型
     * @param payload 消息负载
     */
    public void sendControlMessage(String type, Map<String, Object> payload) {
        Map<String, Object> message = new HashMap<>(payload);
        message.put("type", type);
        message.put("timestamp", System.currentTimeMillis());

        sendMessageToRabbitMQ(message);
    }

    /**
     * 发送消息到 RabbitMQ
     */
    private void sendMessageToRabbitMQ(Map<String, Object> message) {
        try {
            String routingKey = RabbitMQConfig.WEBSOCKET_CONTROL_ROUTING_KEY;
            
            log.info("📤 发送控制消息到 RabbitMQ: {}", message.get("type"));
            
            rabbitTemplate.convertAndSend(
                RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE,
                routingKey,
                message
            );
            
            log.info("✅ 控制消息发送成功");
            
        } catch (Exception e) {
            log.error("❌ 发送控制消息失败", e);
            throw new RuntimeException("发送控制消息失败", e);
        }
    }
}
