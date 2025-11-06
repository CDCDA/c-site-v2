package com.pw.controller;

import com.pw.common.handler.CustomWebSocketHandler;
import com.pw.common.utils.Result;
import com.pw.domain.DiskInfo;
import com.pw.domain.Notification;
import com.pw.domain.Todo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@Tag(name = "webSocket连接")
@RequestMapping("/websocket/channel")
@Slf4j
public class WebSocketController {

    @Autowired
    private CustomWebSocketHandler webSocketHandler;

    /**
     * 发送磁盘信息通知
     */
    @PostMapping("/notification")
    @Operation(summary = "发送信息通知")
    public Result sendNotification(@RequestBody Notification notification) {
        String msg = "信息发送成功";
        switch (notification.getChannels()) {
            case "disk_info":
                webSocketHandler.sendDiskInfo(notification);
                msg = "磁盘信息发送成功";
                break;
            case "todo_list":
                webSocketHandler.sendTodoNotification(notification);
                msg = "待办事项发送成功";
                break;
            case "system_notice":
                webSocketHandler.sendSystemNotice(notification);
                msg = "系统通知发送成功";
                break;
            default:
                log.error("未知的频道类型: {}", notification.getChannels());
        }
        return Result.ok().data(msg);
    }

//    /**
//     * 发送待办事项通知
//     */
//    @PostMapping("/todo-notification")
//    @Operation(summary = "发送待办事项通知")
//    public Result sendTodoNotification(@RequestBody Notification notification) {
//        webSocketHandler.sendTodoNotification(notification);
//        return Result.ok().data("待办事项发送成功");
//    }
//
//    /**
//     * 发送系统通知
//     */
//    @PostMapping("/system-notice")
//    @Operation(summary = "发送系统通知")
//    public Result sendSystemNotice(@RequestBody Notification notification) {
//        webSocketHandler.sendSystemNotice(notification);
//        return Result.ok().data("系统通知发送成功");
//    }
//
//    /**
//     * 发送用户信息
//     */
//    @PostMapping("/user-info/{id}")
//    @Operation(summary = "发送用户信息")
//    public Result sendUserInfo(@PathVariable Long userId, @RequestBody Notification notification) {
//        webSocketHandler.sendMessageToUser(userId, notification);
//        return Result.ok().data("系统通知发送成功");
//    }


    /**
     * 获取频道统计信息
     */
    @GetMapping("/stats")
    @Operation(summary = "获取频道统计信息")
    public Result getChannelStats() {
        Map<String, Integer> stats = webSocketHandler.getChannelStats();
        return Result.ok().data("channelStats", stats);
    }

    /**
     * 获取用户订阅信息
     */
    @GetMapping("/user-subscriptions/{userId}")
    @Operation(summary = "获取用户订阅信息")
    public Result getUserSubscriptions(@PathVariable String userId) {
        Set<String> channels = webSocketHandler.getUserChannels(userId);
        return Result.ok().data("subscriptions", channels);
    }
}
