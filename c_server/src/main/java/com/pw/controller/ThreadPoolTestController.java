package com.pw.controller;

import com.pw.common.util.ThreadPoolUtil;
import com.pw.service.AsyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * 线程池测试控制器
 *
 * @author cyd
 * @date 2025/01/03
 * @description 提供接口演示线程池工具类的使用
 */
@Slf4j
@RestController
@Tag(name = "线程池测试", description = "线程池工具类功能测试接口")
@RequestMapping("/api/threadpool")
public class ThreadPoolTestController {

    /**
     * 测试异步任务（使用工具类）
     */
    @GetMapping("/async")
    @Operation(summary = "测试异步任务")
    public String testAsyncTask(@RequestParam(defaultValue = "测试任务") String taskName) {
        ThreadPoolUtil.asyncExecute(taskName, () -> {
            try {
                Thread.sleep(1000);
                log.info("任务执行中: {}", taskName);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return "异步任务已提交: " + taskName;
    }

    /**
     * 测试长耗时任务（使用工具类）
     */
    @GetMapping("/long-task")
    @Operation(summary = "测试长耗时任务")
    public CompletableFuture<String> testLongTask(@RequestParam(defaultValue = "TASK-001") String taskId) {
        log.info("接收到长耗时任务请求: {}", taskId);
        return ThreadPoolUtil.asyncExecuteLong(taskId, () -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }

    /**
     * 测试手动提交任务（使用工具类）
     */
    @GetMapping("/manual")
    @Operation(summary = "测试手动提交任务")
    public String testManualSubmit(@RequestParam(defaultValue = "手动任务") String taskName) {
        ThreadPoolUtil.execute(() -> {
            try {
                log.info("手动提交任务开始执行: {}, 线程: {}", taskName, Thread.currentThread().getName());
                Thread.sleep(500);
                log.info("手动提交任务执行完成: {}, 线程: {}", taskName, Thread.currentThread().getName());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        return "手动任务已提交: " + taskName;
    }

    /**
     * 批量提交任务测试（使用工具类）
     */
    @GetMapping("/batch")
    @Operation(summary = "批量提交任务测试")
    public String testBatchTasks(@RequestParam(defaultValue = "5") int count) {
        log.info("开始批量提交 {} 个任务", count);
        Runnable[] tasks = new Runnable[count];
        for (int i = 0; i < count; i++) {
            final int index = i;
            tasks[i] = () -> {
                try {
                    Thread.sleep(1000);
                    log.info("批量任务 {} 执行中", index + 1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            };
        }
        ThreadPoolUtil.executeBatch("批量任务", tasks);
        return "已批量提交 " + count + " 个任务";
    }

    /**
     * 测试有返回值的任务
     */
    @GetMapping("/supply")
    @Operation(summary = "测试有返回值的任务")
    public CompletableFuture<String> testSupplyTask(@RequestParam(defaultValue = "计算任务") String taskName) {
        return ThreadPoolUtil.supply(() -> {
            try {
                Thread.sleep(500);
                return taskName + " 执行成功";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return taskName + " 执行失败";
            }
        });
    }

    /**
     * 测试长任务有返回值
     */
    @GetMapping("/supply-long")
    @Operation(summary = "测试长任务有返回值")
    public CompletableFuture<String> testSupplyLongTask(@RequestParam(defaultValue = "长计算任务") String taskName) {
        return ThreadPoolUtil.supplyLongTask(() -> {
            try {
                Thread.sleep(3000);
                return taskName + " 执行成功";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return taskName + " 执行失败";
            }
        });
    }
}
