package com.pw.controller;

import com.pw.config.RabbitMQConfig;
import com.pw.service.WebSocketControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * WebSocket 死信队列测试控制器
 * 用于测试 Confirm 回调和死信队列功能
 *
 * @author cyd
 * @create 2026/04/11
 */
@RestController
@Tag(name = "WebSocket死信队列测试", description = "WebSocket消息队列和死信队列功能测试接口")
@RequestMapping("/api/websocket/dlq-test")
@Slf4j
public class WebSocketDLQTestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebSocketControlService webSocketControlService;

    /**
     * 测试 Confirm 回调 - 发送消息到正确的交换机和路由键
     */
    @PostMapping("/test-confirm-success")
    @Operation(summary = "测试Confirm回调-成功场景")
    public Map<String, Object> testConfirmSuccess() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "test_confirm");
            message.put("content", "测试 Confirm 回调 - 成功");
            message.put("timestamp", System.currentTimeMillis());

            // 生成唯一的 Correlation ID
            String correlationId = UUID.randomUUID().toString();

            // 使用 CorrelationData 发送消息
            CorrelationData correlationData = new CorrelationData(correlationId);

            log.info("📤 发送测试消息，correlationId: {}", correlationId);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE,
                    RabbitMQConfig.WEBSOCKET_BROADCAST_ROUTING_KEY,
                    message,
                    correlationData
            );

            result.put("success", true);
            result.put("message", "测试消息已发送，请查看日志中的 Confirm 回调结果");
            result.put("correlationId", correlationId);

        } catch (Exception e) {
            log.error("发送测试消息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 测试 Returns 回调 - 发送消息到不存在的路由键
     */
    @PostMapping("/test-returns")
    @Operation(summary = "测试Returns回调-失败场景")
    public Map<String, Object> testReturns() {
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> message = new HashMap<>();
            message.put("type", "test_returns");
            message.put("content", "测试 Returns 回调 - 消息无法路由");
            message.put("timestamp", System.currentTimeMillis());

            log.info("📤 发送测试消息到不存在的路由键");

            // 发送到存在的交换机，但不存在的路由键
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE,
                    "nonexistent.routing.key",
                    message
            );

            result.put("success", true);
            result.put("message", "测试消息已发送到不存在的路由键，请查看日志中的 Returns 回调");

        } catch (Exception e) {
            log.error("发送测试消息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 测试死信队列 - 发送消息但不确认（模拟消费失败）
     */
    @PostMapping("/test-dlq/{userId}")
    @Operation(summary = "测试死信队列功能")
    public Map<String, Object> testDLQ(@PathVariable Long userId) {
        Map<String, Object> result = new HashMap<>();

        try {
            // 发送用户消息
            webSocketControlService.sendUserMessage(userId, "测试死信队列消息");

            result.put("success", true);
            result.put("message", "已发送测试消息，如果消息处理失败将进入死信队列");
            result.put("tip", "请查看日志中死信队列监听器的输出");

        } catch (Exception e) {
            log.error("发送测试消息失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 测试系统通知（广播）
     */
    @PostMapping("/test-broadcast")
    @Operation(summary = "测试广播消息")
    public Map<String, Object> testBroadcast() {
        Map<String, Object> result = new HashMap<>();

        try {
            webSocketControlService.sendSystemNotice(
                    "测试通知",
                    "这是一条测试系统通知消息，用于测试 Confirm 回调和死信队列功能",
                    "primary"
            );

            result.put("success", true);
            result.put("message", "系统通知已发送，请查看日志中的 Confirm 回调结果");

        } catch (Exception e) {
            log.error("发送系统通知失败", e);
            result.put("success", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    /**
     * 查看配置信息
     */
    @GetMapping("/config-info")
    @Operation(summary = "查看队列配置信息")
    public Map<String, Object> getConfigInfo() {
        Map<String, Object> configInfo = new HashMap<>();

        configInfo.put("控制总线交换机", RabbitMQConfig.WEBSOCKET_CONTROL_EXCHANGE);
        configInfo.put("广播交换机", RabbitMQConfig.WEBSOCKET_BROADCAST_EXCHANGE);
        configInfo.put("控制队列", RabbitMQConfig.WEBSOCKET_CONTROL_QUEUE);
        configInfo.put("广播队列", RabbitMQConfig.WEBSOCKET_BROADCAST_QUEUE);
        configInfo.put("控制队列死信交换机", RabbitMQConfig.WEBSOCKET_CONTROL_DLX);
        configInfo.put("控制队列死信队列", RabbitMQConfig.WEBSOCKET_CONTROL_DLQ);
        configInfo.put("广播队列死信交换机", RabbitMQConfig.WEBSOCKET_BROADCAST_DLX);
        configInfo.put("广播队列死信队列", RabbitMQConfig.WEBSOCKET_BROADCAST_DLQ);

        return configInfo;
    }
}
