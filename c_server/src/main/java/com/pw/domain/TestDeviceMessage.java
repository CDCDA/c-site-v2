package com.pw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 测试设备消息（模拟SFTP文件传输场景）
 *
 * @author cyd
 * @create 2026/04/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestDeviceMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 用户ID（模拟设备SN）
     */
    private String userId;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件内容（模拟）
     */
    private String fileContent;

    /**
     * 文件大小
     */
    private Long fileSize;

    /**
     * 操作类型：DOWNLOAD, UPLOAD, DELETE
     */
    private String operation;

    /**
     * 发送时间
     */
    private LocalDateTime sendTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 创建消息
     */
    public static TestDeviceMessage create(String userId, String fileName, String operation) {
        return TestDeviceMessage.builder()
                .messageId(java.util.UUID.randomUUID().toString())
                .userId(userId)
                .fileName(fileName)
                .fileContent("模拟文件内容_" + fileName + "_" + System.currentTimeMillis())
                .fileSize(1024L)
                .operation(operation)
                .sendTime(LocalDateTime.now())
                .retryCount(0)
                .build();
    }

    /**
     * 增加重试次数
     */
    public TestDeviceMessage incrementRetry() {
        this.retryCount = this.retryCount == null ? 1 : this.retryCount + 1;
        return this;
    }
}
