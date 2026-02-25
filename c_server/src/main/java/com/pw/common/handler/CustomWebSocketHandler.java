package com.pw.common.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pw.monitor.service.DiskMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class CustomWebSocketHandler extends TextWebSocketHandler {

    // 用户会话存储：userId -> WebSocketSession
    private static final Map<String, WebSocketSession> userSessions = new ConcurrentHashMap<>();

    // 频道订阅关系：channel -> Set<userId>
    private static final Map<String, Set<String>> channelSubscribers = new ConcurrentHashMap<>();

    // 用户订阅的频道：userId -> Set<channel>
    private static final Map<String, Set<String>> userChannels = new ConcurrentHashMap<>();

    private DiskMonitorService diskMonitorService;

    // 构造器注入，DiskMonitorService 使用 @Lazy
    public CustomWebSocketHandler(@Lazy DiskMonitorService diskMonitorService) {
        this.diskMonitorService = diskMonitorService;
    }

    // 定义频道常量
    public static class Channels {
        public static final String DISK_INFO = "disk_info";        // 磁盘信息频道
        public static final String TODO_LIST = "todo_list";        // 待办事项频道
        public static final String SYSTEM_NOTICE = "system_notice"; // 系统通知频道
        public static final String USER_MESSAGE = "user_message";   // 用户消息频道
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String sessionId = session.getId();
        String userId = getUserIdFromSession(session);

        if (userId != null) {
            userSessions.put(userId, session);
            userChannels.putIfAbsent(userId, ConcurrentHashMap.newKeySet());

            log.info("🎉 用户连接 - 用户ID: {}, 会话ID: {}", userId, sessionId);
            log.info("🔗 当前在线用户数: {}", userSessions.size());

            // 发送连接成功消息
            sendJsonMessage(session, Map.of(
                    "type", "connected",
                    "userId", userId,
                    "message", "连接成功",
                    "availableChannels", getAvailableChannels()
            ));

        } else {
            log.warn("⚠️ 未识别用户连接，关闭会话: {}", sessionId);
            session.close();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String sessionId = session.getId();
        String userId = getUserBySessionId(sessionId);

        if (userId == null) {
            session.sendMessage(new TextMessage("ERROR: 用户未认证"));
            return;
        }

        String payload = message.getPayload();
        log.info("📩 收到消息 - 用户: {}, 内容: {}", userId, payload);

        handleUserMessage(userId, payload, session);
    }

    /**
     * 处理用户消息 - 主要是订阅/取消订阅操作
     */
    private void handleUserMessage(String userId, String payload, WebSocketSession session) throws IOException {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            String type = jsonNode.has("type") ? jsonNode.get("type").asText() : "unknown";
            String channel = jsonNode.has("channel") ? jsonNode.get("channel").asText() : "";
            String action = jsonNode.has("action") ? jsonNode.get("action").asText() : "subscribe";

            switch (type) {
                case "subscribe":
                    if ("unsubscribe".equals(action)) {
                        unsubscribeChannel(userId, channel, session);
                    } else {
                        subscribeChannel(userId, channel, session);
                    }
                    break;

                case "get_subscriptions":
                    sendUserSubscriptions(userId, session);
                    break;

                case "get_available_channels":
                    sendJsonMessage(session, Map.of(
                            "type", "available_channels",
                            "channels", getAvailableChannels()
                    ));
                    break;

                default:
                    sendJsonMessage(session, Map.of(
                            "type", "error",
                            "message", "未知的消息类型: " + type
                    ));
            }
        } catch (Exception e) {
            log.error("处理用户消息失败", e);
            sendJsonMessage(session, Map.of(
                    "type", "error",
                    "message", "消息格式错误"
            ));
        }
    }

    /**
     * 订阅频道
     */
    private void subscribeChannel(String userId, String channel, WebSocketSession session) throws IOException {
        if (!isValidChannel(channel)) {
            sendJsonMessage(session, Map.of(
                    "type", "error",
                    "message", "无效的频道: " + channel
            ));
            return;
        }

        // 添加用户到频道
        channelSubscribers.computeIfAbsent(channel, k -> ConcurrentHashMap.newKeySet())
                .add(userId);

        // 添加频道到用户
        userChannels.computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(channel);

        log.info("✅ 用户 {} 订阅频道: {}", userId, channel);

        sendJsonMessage(session, Map.of(
                "type", "subscribed",
                "channel", channel,
                "message", "成功订阅频道: " + channel
        ));

        // 可选：订阅后立即发送该频道的最新信息
//        sendChannelWelcomeMessage(userId, channel);
    }

    /**
     * 取消订阅频道
     */
    private void unsubscribeChannel(String userId, String channel, WebSocketSession session) throws IOException {
        // 从频道中移除用户
        if (channelSubscribers.containsKey(channel)) {
            channelSubscribers.get(channel).remove(userId);
        }

        // 从用户中移除频道
        if (userChannels.containsKey(userId)) {
            userChannels.get(userId).remove(channel);
        }

        log.info("❌ 用户 {} 取消订阅频道: {}", userId, channel);

        sendJsonMessage(session, Map.of(
                "type", "unsubscribed",
                "channel", channel,
                "message", "已取消订阅频道: " + channel
        ));
    }

    /**
     * 向频道发送消息
     */
    public void sendMessageToChannel(String channel, Object message) {
        Set<String> subscribers = channelSubscribers.get(channel);
        if (subscribers == null || subscribers.isEmpty()) {
            log.warn("📭 频道 {} 没有订阅者", channel);
            return;
        }

        log.info("📢 向频道 {} 发送消息给 {} 个订阅者: {}", channel, subscribers.size(), message);

        String messageJson;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> messageMap = new HashMap<>();
            messageMap.put("type", "channel_message");
            messageMap.put("channel", channel);
            messageMap.put("timestamp", System.currentTimeMillis());
            messageMap.put("data", message);
            messageJson = objectMapper.writeValueAsString(messageMap);
        } catch (Exception e) {
            log.error("消息序列化失败", e);
            return;
        }

        subscribers.forEach(userId -> {
            WebSocketSession session = userSessions.get(userId);
            if (session != null && session.isOpen()) {
                try {
                    session.sendMessage(new TextMessage(messageJson));
                } catch (IOException e) {
                    log.error("向用户 {} 发送频道消息失败", userId, e);
                }
            }
        });
    }

    /**
     * 向指定用户发送消息
     */
    public void sendMessageToUser(Long userId, Object message) {
        sendMessageToChannel(Channels.USER_MESSAGE, Map.of(
                "targetUser", userId,
                "message", message
        ));
    }

    /**
     * 发送磁盘信息通知
     */
    public void sendDiskInfo(Object diskInfo) {
        sendMessageToChannel(Channels.DISK_INFO, diskInfo);
    }

    /**
     * 发送待办事项通知
     */
    public void sendTodoNotification(Object todoInfo) {
        sendMessageToChannel(Channels.TODO_LIST, todoInfo);
    }

    /**
     * 发送系统通知
     */
    public void sendSystemNotice(Object notice) {
        sendMessageToChannel(Channels.SYSTEM_NOTICE, notice);
    }

    /**
     * 获取用户订阅的频道
     */
    public Set<String> getUserChannels(String userId) {
        return userChannels.getOrDefault(userId, Collections.emptySet());
    }

    /**
     * 获取频道的订阅者数量
     */
    public int getChannelSubscriberCount(String channel) {
        Set<String> subscribers = channelSubscribers.get(channel);
        return subscribers != null ? subscribers.size() : 0;
    }

    /**
     * 获取所有频道的订阅统计
     */
    public Map<String, Integer> getChannelStats() {
        Map<String, Integer> stats = new HashMap<>();
        channelSubscribers.forEach((channel, subscribers) -> {
            stats.put(channel, subscribers.size());
        });
        return stats;
    }

    // 辅助方法
    private String getUserIdFromSession(WebSocketSession session) {
        // 根据你的业务逻辑实现用户识别
        // 这里使用sessionId作为示例
        return "user_" + session.getId();
    }

    private String getUserBySessionId(String sessionId) {
        return userSessions.entrySet().stream()
                .filter(entry -> entry.getValue().getId().equals(sessionId))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }

    private boolean isValidChannel(String channel) {
        return Arrays.asList(
                Channels.DISK_INFO,
                Channels.TODO_LIST,
                Channels.SYSTEM_NOTICE,
                Channels.USER_MESSAGE
        ).contains(channel);
    }

    private Map<String, String> getAvailableChannels() {
        Map<String, String> channels = new HashMap<>();
        channels.put(Channels.DISK_INFO, "磁盘信息");
        channels.put(Channels.TODO_LIST, "待办事项");
        channels.put(Channels.SYSTEM_NOTICE, "系统通知");
        channels.put(Channels.USER_MESSAGE, "用户消息");
        return channels;
    }

    private void sendUserSubscriptions(String userId, WebSocketSession session) throws IOException {
        Set<String> channels = getUserChannels(userId);
        sendJsonMessage(session, Map.of(
                "type", "user_subscriptions",
                "channels", channels
        ));
    }

    private void sendChannelWelcomeMessage(String userId, String channel) throws IOException {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            switch (channel) {
                case Channels.DISK_INFO:
                    diskMonitorService.getCurrentDiskInfoAndSend();
                    break;
                case Channels.TODO_LIST:
                    sendJsonMessage(session, Map.of(
                            "type", "channel_welcome",
                            "channel", channel,
                            "message", "欢迎订阅待办事项频道，将实时接收任务通知"
                    ));
                    break;
            }
        }
    }

    private void sendJsonMessage(WebSocketSession session, Object data) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(data);
        session.sendMessage(new TextMessage(json));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionId = session.getId();
        String userId = getUserBySessionId(sessionId);

        if (userId != null) {
            // 清理用户的所有订阅
            Set<String> channels = userChannels.remove(userId);
            if (channels != null) {
                channels.forEach(channel -> {
                    if (channelSubscribers.containsKey(channel)) {
                        channelSubscribers.get(channel).remove(userId);
                    }
                });
            }

            userSessions.remove(userId);
            log.info("👋 用户断开连接 - 用户ID: {}, 原因: {}", userId, status);
            log.info("🔗 剩余在线用户数: {}", userSessions.size());
        }
    }
}
