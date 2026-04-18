package com.pw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 线程池使用示例服务
 *
 * @author cyd
 * @date 2026/04/03
 * @description 演示如何使用自定义线程池执行异步任务
 */
@Slf4j
@Service
public class AsyncService {

    /**
     * 使用默认线程池执行异步任务
     * @Async 注解会将方法在异步线程中执行
     */
    @Async
    public void executeAsyncTask(String taskName) {
        log.info("开始执行异步任务: {}, 线程: {}", taskName, Thread.currentThread().getName());
        try {
            // 模拟任务执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("异步任务执行完成: {}, 线程: {}", taskName, Thread.currentThread().getName());
    }

    /**
     * 使用指定的线程池执行异步任务
     * @Qualifier("taskExecutor") 指定使用 taskExecutor 线程池
     */
    @Async("taskExecutor")
    public void executeAsyncTaskWithCustomPool(String taskName) {
        log.info("开始执行异步任务: {}, 线程: {}", taskName, Thread.currentThread().getName());
        try {
            // 模拟任务执行
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("异步任务执行完成: {}, 线程: {}", taskName, Thread.currentThread().getName());
    }

    /**
     * 使用长任务线程池执行耗时任务
     */
    @Async("longTaskExecutor")
    public CompletableFuture<String> executeLongTask(String taskId) {
        log.info("开始执行长耗时任务: {}, 线程: {}", taskId, Thread.currentThread().getName());
        try {
            // 模拟长耗时任务
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("长耗时任务执行完成: {}, 线程: {}", taskId, Thread.currentThread().getName());
        return CompletableFuture.completedFuture("任务 " + taskId + " 执行完成");
    }

    /**
     * 手动使用线程池执行任务
     */
    public void executeWithManualPool(Executor taskExecutor, String taskName) {
        taskExecutor.execute(() -> {
            log.info("手动提交任务开始执行: {}, 线程: {}", taskName, Thread.currentThread().getName());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            log.info("手动提交任务执行完成: {}, 线程: {}", taskName, Thread.currentThread().getName());
        });
    }
}
