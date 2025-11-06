package com.pw.monitor.service;

/***
 * @author cyd
 * @date 2025/10/27 13:42
 * @description <>
 **/

import com.pw.common.handler.CustomWebSocketHandler;
import com.pw.domain.Notification;
import com.pw.monitor.DiskSpaceMonitor;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.pw.domain.DiskInfo;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiskMonitorService {

    private final DiskSpaceMonitor diskSpaceMonitor;
    private CustomWebSocketHandler webSocketHandler;

    // 使用 Setter 注入
    @Autowired
    public void setWebSocketHandler(CustomWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    private boolean lastWarningState = false;

    // 每5分钟检查一次
    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void monitorDiskSpace() {
        String diskPath = "/"; // 监控根目录，可根据需要修改

        boolean isLowSpace = diskSpaceMonitor.isDiskSpaceLow(diskPath);
        DiskInfo diskInfo = diskSpaceMonitor.getDiskInfo(diskPath);

        log.info("磁盘监控 - 总量: {}, 可用: {}, 使用率: {}",
                diskInfo.getTotalSpace(), diskInfo.getFreeSpace(), diskInfo.getUsageRate());

        // 只在状态变化时发送警告
        if (isLowSpace) {
            String warningMessage = String.format(
                    "⚠️ 磁盘空间警告！\n" +
                            "磁盘空间不足，请及时清理！\n" +
                            "总量: %s\n" +
                            "可用: %s\n" +
                            "使用率: %s",
                    diskInfo.getTotalSpace(), diskInfo.getFreeSpace(), diskInfo.getUsageRate()
            );
            Notification notification = new Notification();
            notification.setTitle("磁盘信息");
            notification.setStatus("primary");
            notification.setChannels("disk_info");
            notification.setContent(warningMessage);
            webSocketHandler.sendDiskInfo(notification);
            log.warn("磁盘空间不足警告已发送: {}", warningMessage);
        }

        lastWarningState = isLowSpace;
    }


    // 新增方法：获取当前磁盘信息并发送
    public void getCurrentDiskInfoAndSend() {
        String diskPath = "/"; // 监控根目录，可根据需要修改
        DiskInfo diskInfo = diskSpaceMonitor.getDiskInfo(diskPath);

        // 构建磁盘信息消息
        String message = String.format(
                "当前磁盘状态\n" +
                        "总量: %s\n" +
                        "可用: %s\n" +
                        "使用率: %s\n" +
                        "状态: %s",
                diskInfo.getTotalSpace(),
                diskInfo.getFreeSpace(),
                diskInfo.getUsageRate(),
                diskInfo.isLowSpace() ? "⚠️ 空间不足" : "✅ 正常"
        );
        Notification notification = new Notification();
        notification.setTitle("磁盘信息频道订阅成功");
        notification.setStatus("primary");
        notification.setChannels("disk_info");
        notification.setContent(message);
        // 发送消息
        webSocketHandler.sendDiskInfo(notification);
        log.info("已主动发送磁盘信息: {}", message);
    }
}
