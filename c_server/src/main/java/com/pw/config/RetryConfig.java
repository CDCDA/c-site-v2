package com.pw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.backoff.Sleeper;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.Random;

/**
 * 重试配置类
 * 配置带有随机波动的指数退避重试策略
 *
 * 重试间隔：1s, 2s, 4s, 8s, 16s（指数增长）
 * 随机波动：0-500ms
 *
 * @author cyd
 * @create 2026/04/11
 */
@Configuration
@Slf4j
public class RetryConfig {

    private final Random random = new Random();

    /**
     * 创建带有随机波动的 Sleeper
     * 在基础 sleep 时间上添加 0-500ms 的随机波动
     */
    @Bean
    public Sleeper jitterSleeper() {
        log.info("🔧 初始化带有随机波动的 Sleeper");

        return (sleepTime) -> {
            // 添加随机波动：0-500ms
            long jitter = random.nextInt(500);
            long totalSleepTime = sleepTime + jitter;

            if (log.isDebugEnabled()) {
                log.debug("⏱️  指数退避: 基础间隔={}ms, 随机波动={}ms, 总间隔={}ms",
                        sleepTime, jitter, totalSleepTime);
            }

            try {
                Thread.sleep(totalSleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Thread interrupted during sleep", e);
            }
        };
    }

    /**
     * 创建带有随机波动的指数退避策略
     *
     * 重试间隔计算公式：
     * interval = initialInterval * (multiplier ^ (attempt - 1)) + random(0, 500)
     *
     * 例如：
     * 第1次重试：1000ms + random(0, 500) = 1000-1500ms
     * 第2次重试：2000ms + random(0, 500) = 2000-2500ms
     * 第3次重试：4000ms + random(0, 500) = 4000-4500ms
     * 第4次重试：8000ms + random(0, 500) = 8000-8500ms
     * 第5次重试：16000ms + random(0, 500) = 16000-16500ms
     */
    @Bean
    public ExponentialBackOffPolicy exponentialBackOffWithJitter() {
        log.info("🔧 初始化带有随机波动的指数退避重试策略");

        // 创建指数退避策略
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();

        // 设置自定义的 Sleeper（带有随机波动）
        backOffPolicy.setSleeper(jitterSleeper());

        // 初始间隔：1秒
        backOffPolicy.setInitialInterval(1000L);

        // 乘数：每次重试间隔乘以2（指数增长）
        backOffPolicy.setMultiplier(2.0);

        // 最大间隔：16秒
        backOffPolicy.setMaxInterval(16000L);

        return backOffPolicy;
    }

    /**
     * 创建 RetryTemplate Bean
     * 用于手动重试场景
     */
    @Bean
    public RetryTemplate retryTemplate() {
        log.info("🔧 初始化 RetryTemplate");

        RetryTemplate retryTemplate = new RetryTemplate();

        // 设置重试策略：最多重试5次
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(5);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 设置退避策略：带有随机波动的指数退避
        retryTemplate.setBackOffPolicy(exponentialBackOffWithJitter());

        log.info("✅ RetryTemplate 初始化完成 - 最多重试5次，使用指数退避（1s, 2s, 4s, 8s, 16s）+ 随机波动（0-500ms）");

        return retryTemplate;
    }
}
