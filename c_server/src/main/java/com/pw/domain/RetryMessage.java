package com.pw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 重试消息包装类
 * 用于 RabbitMQ 延迟队列重试机制
 *
 * @author cyd
 * @create 2026/04/11
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryMessage {

    /**
     * 原始消息内容
     */
    private Map<String, Object> message;

    /**
     * 目标交换机（最终要发送到的交换机）
     */
    private String targetExchange;

    /**
     * 目标路由键（最终要发送到的路由键）
     */
    private String targetRoutingKey;

    /**
     * 当前重试次数（从0开始）
     */
    private int retryCount;

    /**
     * 原始消息ID（用于追踪和去重）
     */
    private String originalMessageId;

    /**
     * 首次失败时间戳
     */
    private long firstFailTime;

    /**
     * 错误信息
     */
    private String errorMessage;

    /**
     * 最大重试次数（固定为8）
     */
    public static final int MAX_RETRY_COUNT = 4;

    /**
     * 重试间隔数组（毫秒）：1s, 2s, 4s, 8s, 16s, 32s, 64s, 128s
     */
    public static final long[] RETRY_DELAYS = {
        1000L,   // 1秒
        2000L,   // 2秒
        4000L,   // 4秒
        8000L,   // 8秒
        16000L,  // 16秒
        32000L,  // 32秒
        64000L,  // 64秒
        128000L  // 128秒
    };

    /**
     * 判断是否达到最大重试次数
     */
    public boolean isMaxRetryReached() {
        return retryCount >= MAX_RETRY_COUNT;
    }

    /**
     * 获取当前重试对应的延迟时间
     */
    public long getCurrentDelay() {
        if (retryCount < RETRY_DELAYS.length) {
            return RETRY_DELAYS[retryCount];
        }
        return RETRY_DELAYS[RETRY_DELAYS.length - 1];
    }

    /**
     * 生成重试消息（用于第一次重试）
     */
    public static RetryMessage createForFirstRetry(
            Map<String, Object> message,
            String targetExchange,
            String targetRoutingKey,
            String errorMessage) {
        RetryMessage retryMessage = new RetryMessage();
        retryMessage.setMessage(message);
        retryMessage.setTargetExchange(targetExchange);
        retryMessage.setTargetRoutingKey(targetRoutingKey);
        retryMessage.setRetryCount(0);
        retryMessage.setOriginalMessageId(java.util.UUID.randomUUID().toString());
        retryMessage.setFirstFailTime(System.currentTimeMillis());
        retryMessage.setErrorMessage(errorMessage);
        return retryMessage;
    }

    /**
     * 创建下一次重试的消息副本
     */
    public RetryMessage createNextRetry() {
        RetryMessage nextRetry = new RetryMessage();
        nextRetry.setMessage(this.message);
        nextRetry.setTargetExchange(this.targetExchange);
        nextRetry.setTargetRoutingKey(this.targetRoutingKey);
        nextRetry.setRetryCount(this.retryCount + 1);
        nextRetry.setOriginalMessageId(this.originalMessageId);
        nextRetry.setFirstFailTime(this.firstFailTime);
        nextRetry.setErrorMessage(this.errorMessage);
        return nextRetry;
    }
}
