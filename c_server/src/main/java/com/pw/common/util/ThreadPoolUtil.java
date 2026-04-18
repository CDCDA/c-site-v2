package com.pw.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 线程池工具类
 *
 * @author cyd
 * @date 2026/04/10
 * @description 提供便捷的线程池访问方法，无需通过构造函数或@Autowired注入
 */
@Slf4j
@Component
public class ThreadPoolUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    /**
     * 获取默认线程池（taskExecutor）
     */
    public static Executor getTaskExecutor() {
        return applicationContext.getBean("taskExecutor", Executor.class);
    }

    /**
     * 获取长任务线程池（longTaskExecutor）
     */
    public static Executor getLongTaskExecutor() {
        return applicationContext.getBean("longTaskExecutor", Executor.class);
    }

    /**
     * 使用默认线程池执行任务
     */
    public static void execute(Runnable task) {
        getTaskExecutor().execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("线程池任务执行异常", e);
            }
        });
    }

    /**
     * 使用长任务线程池执行任务
     */
    public static void executeLongTask(Runnable task) {
        getLongTaskExecutor().execute(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("长任务线程池任务执行异常", e);
            }
        });
    }

    /**
     * 使用默认线程池提交任务并返回CompletableFuture
     */
    public static CompletableFuture<Void> submit(Runnable task) {
        return CompletableFuture.runAsync(() -> {
            try {
                task.run();
            } catch (Exception e) {
                log.error("线程池任务执行异常", e);
            }
        }, getTaskExecutor());
    }

    /**
     * 使用默认线程池提交有返回值的任务
     */
    public static <T> CompletableFuture<T> supply(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.get();
            } catch (Exception e) {
                log.error("线程池任务执行异常", e);
                throw new RuntimeException("任务执行失败", e);
            }
        }, getTaskExecutor());
    }

    /**
     * 使用长任务线程池提交有返回值的任务
     */
    public static <T> CompletableFuture<T> supplyLongTask(java.util.function.Supplier<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.get();
            } catch (Exception e) {
                log.error("长任务线程池任务执行异常", e);
                throw new RuntimeException("长任务执行失败", e);
            }
        }, getLongTaskExecutor());
    }

    /**
     * 异步执行任务（不关心结果）
     *
     * @param taskName 任务名称（用于日志）
     * @param task     任务逻辑
     */
    public static void asyncExecute(String taskName, Runnable task) {
        getTaskExecutor().execute(() -> {
            log.info("开始执行异步任务: {}, 线程: {}", taskName, Thread.currentThread().getName());
            try {
                long startTime = System.currentTimeMillis();
                task.run();
                long duration = System.currentTimeMillis() - startTime;
                log.info("异步任务执行完成: {}, 耗时: {}ms, 线程: {}", taskName, duration, Thread.currentThread().getName());
            } catch (Exception e) {
                log.error("异步任务执行失败: {}, 线程: {}", taskName, Thread.currentThread().getName(), e);
            }
        });
    }

    /**
     * 异步执行长耗时任务
     *
     * @param taskName 任务名称（用于日志）
     * @param task     任务逻辑
     */
    public static CompletableFuture<String> asyncExecuteLong(String taskName, Runnable task) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("开始执行长耗时任务: {}, 线程: {}", taskName, Thread.currentThread().getName());
            try {
                long startTime = System.currentTimeMillis();
                task.run();
                long duration = System.currentTimeMillis() - startTime;
                log.info("长耗时任务执行完成: {}, 耗时: {}ms, 线程: {}", taskName, duration, Thread.currentThread().getName());
                return "任务 " + taskName + " 执行完成";
            } catch (Exception e) {
                log.error("长耗时任务执行失败: {}, 线程: {}", taskName, Thread.currentThread().getName(), e);
                throw new RuntimeException("任务执行失败", e);
            }
        }, getLongTaskExecutor());
    }

    /**
     * 批量执行任务
     *
     * @param taskPrefix 任务名称前缀
     * @param tasks      任务列表
     */
    public static void executeBatch(String taskPrefix, Runnable... tasks) {
        for (int i = 0; i < tasks.length; i++) {
            final int index = i;
            final String taskName = taskPrefix + "-" + (index + 1);
            getTaskExecutor().execute(() -> {
                try {
                    log.info("批量任务开始执行: {}, 线程: {}", taskName, Thread.currentThread().getName());
                    long startTime = System.currentTimeMillis();
                    tasks[index].run();
                    long duration = System.currentTimeMillis() - startTime;
                    log.info("批量任务执行完成: {}, 耗时: {}ms, 线程: {}", taskName, duration, Thread.currentThread().getName());
                } catch (Exception e) {
                    log.error("批量任务执行失败: {}, 线程: {}", taskName, Thread.currentThread().getName(), e);
                }
            });
        }
    }
}
