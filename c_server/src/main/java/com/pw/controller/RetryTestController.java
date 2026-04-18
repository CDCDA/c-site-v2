package com.pw.controller;

import com.pw.service.WebSocketControlService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 重试机制测试控制器
 * 演示 RabbitMQ 延迟队列非阻塞重试机制
 *
 * @author cyd
 * @create 2026/04/11
 */
@RestController
@RequestMapping("/api/retry-test")
@Slf4j
public class RetryTestController {

    @Autowired
    private WebSocketControlService webSocketControlService;

    /**
     * 测试 WebSocket 消息发送的非阻塞重试机制
     * 发送失败时，消息会自动进入延迟队列进行非阻塞重试
     */
    @PostMapping("/websocket-retry")
    public Map<String, Object> testWebSocketRetry() {
        Map<String, Object> result = new HashMap<>();

        try {
            webSocketControlService.sendSystemNotice(
                    "非阻塞重试测试通知",
                    "这是一条测试消息，用于演示 RabbitMQ 延迟队列非阻塞重试机制（1s, 2s, 4s, 8s, 16s, 32s, 64s, 128s）",
                    "primary"
            );

            result.put("success", true);
            result.put("message", "消息已发送（如发送失败将自动进入延迟队列重试）");

        } catch (Exception e) {
            log.error("消息发送异常", e);
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("message", "消息发送异常");
        }

        return result;
    }

    /**
     * 测试 WebSocket 广播消息的重试
     */
    @PostMapping("/broadcast-retry")
    public Map<String, Object> testBroadcastRetry() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> broadcastData = new HashMap<>();
            broadcastData.put("message", "这是一条测试广播消息，演示非阻塞重试机制");
            broadcastData.put("timestamp", System.currentTimeMillis());

            webSocketControlService.sendBroadcastMessage("retry_test_channel", broadcastData);

            result.put("success", true);
            result.put("message", "广播消息已发送（如发送失败将自动进入延迟队列重试）");

        } catch (Exception e) {
            log.error("广播消息发送失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 获取重试策略说明
     */
    @GetMapping("/retry-info")
    public Map<String, Object> getRetryInfo() {
        Map<String, Object> info = new HashMap<>();

        info.put("重试类型", "RabbitMQ 延迟队列非阻塞重试（Non-blocking Retry with Delay Queue）");

        java.util.List<String> advantages = java.util.Arrays.asList(
                "完全非阻塞，线程立即释放",
                "适用于高并发场景（大量文件处理）",
                "重试状态持久化，应用重启不丢失",
                "支持分布式部署",
                "提高系统吞吐量"
        );
        info.put("优点", advantages);

        Map<String, String> schedule = new HashMap<>();
        schedule.put("第1次重试", "1秒后");
        schedule.put("第2次重试", "2秒后");
        schedule.put("第3次重试", "4秒后");
        schedule.put("第4次重试", "8秒后");
        schedule.put("第5次重试", "16秒后");
        schedule.put("第6次重试", "32秒后");
        schedule.put("第7次重试", "64秒后");
        schedule.put("第8次重试", "128秒后");

        info.put("重试时间表", schedule);

        Map<String, String> config = new HashMap<>();
        config.put("maxAttempts", "8 (最多重试8次)");
        config.put("delayPattern", "指数退避 (1s, 2s, 4s, 8s, 16s, 32s, 64s, 128s)");
        config.put("blocking", "false (完全非阻塞)");
        config.put("persistence", "true (消息持久化)");
        config.put("distributed", "true (支持分布式)");
        info.put("配置", config);

        return info;
    }

    /**
     * 获取重试延迟时间预览
     */
    @GetMapping("/delay-preview")
    public Map<String, Object> getDelayPreview() {
        Map<String, Object> result = new HashMap<>();

        Map<Integer, Long> retryDelays = new HashMap<>();
        for (int i = 1; i <= 8; i++) {
            long delay = (long) Math.pow(2, i - 1) * 1000;
            retryDelays.put(i, delay);
        }

        result.put("success", true);
        result.put("retryDelays", retryDelays);

        return result;
    }
}
