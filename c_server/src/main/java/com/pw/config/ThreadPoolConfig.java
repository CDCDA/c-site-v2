package com.pw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置类
 *
 * @author cyd
 * @date 2025/01/03
 * @description 自定义线程池配置，用于异步任务执行
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig {

    /**
     * 核心线程数
     */
    private static final int CORE_POOL_SIZE = 15;

    /**
     * 最大线程数
     */
    private static final int MAX_POOL_SIZE = 30;

    /**
     * 队列容量
     */
    private static final int QUEUE_CAPACITY = 300;

    /**
     * 线程空闲时间（秒）
     */
    private static final int KEEP_ALIVE_SECONDS = 60;

    /**
     * 线程名称前缀
     */
    private static final String THREAD_NAME_PREFIX = "core-executor-";

    /**
     * 自定义线程池
     * 使用 @Bean("taskExecutor") 指定 bean 名称，方便在需要的地方通过 @Qualifier("taskExecutor") 注入
     * 移入 ThreadPoolUtil 工具类
     * @return 线程池执行器
     */
    @Bean("taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 配置核心线程数
        executor.setCorePoolSize(CORE_POOL_SIZE);

        // 配置最大线程数
        executor.setMaxPoolSize(MAX_POOL_SIZE);

        // 配置队列容量
        executor.setQueueCapacity(QUEUE_CAPACITY);

        // 配置线程空闲时间
        executor.setKeepAliveSeconds(KEEP_ALIVE_SECONDS);

        // 配置线程名称前缀
        executor.setThreadNamePrefix(THREAD_NAME_PREFIX);

        // 配置拒绝策略：由调用线程处理该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 等待所有任务完成后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);

        // 等待时间（秒）
        executor.setAwaitTerminationSeconds(60);

        // 初始化线程池
        executor.initialize();

        return executor;
    }

    /**
     * 创建另一个专用线程池，用于处理耗时较长的任务
     *
     * @return 线程池执行器
     */
    @Bean("longTaskExecutor")
    public Executor longTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心线程数较少，避免占用过多资源
        executor.setCorePoolSize(10);

        // 最大线程数也较少
        executor.setMaxPoolSize(20);

        // 队列容量较大，适合处理批量任务
        executor.setQueueCapacity(500);

        // 线程空闲时间
        executor.setKeepAliveSeconds(300);

        // 线程名称前缀
        executor.setThreadNamePrefix("long-task-executor-");

        // 拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(300);

        executor.initialize();

        return executor;
    }
}
