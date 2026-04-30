package com.pw.controller;

import com.pw.common.utils.Result;
import com.pw.service.WebSocketControlService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * WebSocket 控制总线控制器
 * 通过 REST API 发送消息到 RabbitMQ，再由 RabbitMQ 转发到 WebSocket
 *
 * @author cyd
 * @create 2026/03/18
 */
@RestController
@Tag(name = "WebSocket 控制总线")
@RequestMapping("/websocket/control")
@Slf4j
public class WebSocketControlController {

    @Autowired
    private WebSocketControlService webSocketControlService;

    /**
     * 广播消息到指定频道
     */
    @PostMapping("/broadcast/{channel}")
    @Operation(summary = "广播消息到指定频道")
    public Result broadcastMessage(
            @PathVariable String channel,
            @RequestBody Object data) {

        log.info("📢 收到广播请求 - 频道：{}, 数据：{}", channel, data);
        webSocketControlService.sendBroadcastMessage(channel, data);

        return Result.ok().data("消息已发送到频道：" + channel);
    }

    /**
     * 发送消息给特定用户
     */
    @PostMapping("/user/{userId}")
    @Operation(summary = "发送消息给特定用户")
    public Result sendUserMessage(
            @PathVariable Long userId,
            @RequestBody Object message) {

        log.info("👤 收到用户消息请求 - 用户 ID: {}, 消息：{}", userId, message);
        webSocketControlService.sendUserMessage(userId, message);

        return Result.ok().data("消息已发送给用户：" + userId);
    }


    /**
     * 发送待办事项通知
     */
    @PostMapping("/todo-notification")
    @Operation(summary = "发送待办事项通知")
    public Result sendTodoNotification(@RequestBody Map<String, Object> todoInfo) {
        log.info("📝 收到待办事项通知：{}", todoInfo);
        webSocketControlService.sendTodoNotification((Long) todoInfo.get("userId"), todoInfo);

        return Result.ok().data("待办事项通知已发送");
    }

    /**
     * 发送自定义控制消息
     */
//    @PostMapping("/custom")
//    @Operation(summary = "发送自定义控制消息")
//    public Result sendCustomMessage(
//            @RequestParam String type,
//            @RequestBody Map<String, Object> payload) {
//
//        log.info("📨 收到自定义消息请求 - 类型：{}, 负载：{}", type, payload);
//        webSocketControlService.sendControlMessage(type, payload);
//
//        return Result.ok().data("自定义消息已发送");
//    }
}
