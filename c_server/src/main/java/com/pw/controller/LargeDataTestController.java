package com.pw.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pw.common.utils.Result;
import com.pw.domain.Order;
import com.pw.domain.TestUser;
import com.pw.service.LargeDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * 大批量数据测试控制器
 *
 * @author cyd
 * @date 2026/04/07
 * @description 提供大批量数据的增删改查测试接口
 */
@Slf4j
@RestController
@RequestMapping("/large-data")
@Tag(name = "大批量数据测试", description = "用于测试大批量数据的增删改查性能")
public class LargeDataTestController {

    private final LargeDataService largeDataService;
    private final Executor longTaskExecutor;

    public LargeDataTestController(
            LargeDataService largeDataService,
            @Qualifier("longTaskExecutor") Executor longTaskExecutor) {
        this.largeDataService = largeDataService;
        this.longTaskExecutor = longTaskExecutor;
    }

    /**
     * 批量插入订单数据
     */
    @GetMapping("/batch-insert-orders")
    @Operation(summary = "批量插入订单数据", description = "批量插入指定数量的订单数据用于测试（支持并行处理）")
    public Result batchInsertOrders(
            @Parameter(description = "插入数量，默认100条，建议：1000、10000、100000")
            @RequestParam(defaultValue = "100") int count,
            @Parameter(description = "是否启用并行处理（每个批次使用独立线程），默认true")
            @RequestParam(defaultValue = "true") boolean parallel) {

        log.info("接收到批量插入订单请求，数量: {}, 并行: {}", count, parallel);

        CompletableFuture future = CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                long insertedCount = largeDataService.batchInsertOrders(count, parallel);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "批量插入订单成功");
                result.put("requestCount", count);
                result.put("insertedCount", insertedCount);
                result.put("parallel", parallel);
                result.put("avgTimePerRecord", String.format("%.2f", executionTime * 1.0 / count) + " ms");
                result.put("executionTime", executionTime + " ms");

                log.info("批量插入订单接口执行完成，耗时: {} ms，模式: {}", executionTime, parallel ? "并行" : "顺序");
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("批量插入订单失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "批量插入订单失败: " + e.getMessage());
                result.put("parallel", parallel);
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
        Map<String, Object> result = new HashMap<>(); // This line is unnecessary
        try {
            result = (Map<String, Object>) future.get();
        } catch (Exception e) {
            log.error("批量插入订单请求处理异常", e);
        }
        return Result.ok().data(result);
    }

    /**
     * 批量插入用户数据
     */
    @GetMapping("/batch-insert-users")
    @Operation(summary = "批量插入用户数据", description = "批量插入指定数量的用户数据用于测试（支持并行处理）")
    public CompletableFuture<Map<String, Object>> batchInsertUsers(
            @Parameter(description = "插入数量，默认100条，建议：1000、10000、100000")
            @RequestParam(defaultValue = "100") int count,
            @Parameter(description = "是否启用并行处理（每个批次使用独立线程），默认true")
            @RequestParam(defaultValue = "true") boolean parallel) {

        log.info("接收到批量插入用户请求，数量: {}, 并行: {}", count, parallel);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                long insertedCount = largeDataService.batchInsertUsers(count, parallel);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "批量插入用户成功");
                result.put("requestCount", count);
                result.put("insertedCount", insertedCount);
                result.put("parallel", parallel);
                result.put("avgTimePerRecord", String.format("%.2f", executionTime * 1.0 / count) + " ms");
                result.put("executionTime", executionTime + " ms");

                log.info("批量插入用户接口执行完成，耗时: {} ms，模式: {}", executionTime, parallel ? "并行" : "顺序");
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("批量插入用户失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "批量插入用户失败: " + e.getMessage());
                result.put("parallel", parallel);
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 查询所有订单
     */
    @GetMapping("/get-all-orders")
    @Operation(summary = "查询所有订单", description = "查询数据库中的订单数据，限制最大返回10000条")
    public CompletableFuture<Map<String, Object>> getAllOrders(
            @Parameter(description = "返回记录数限制，默认10000，最大10000")
            @RequestParam(defaultValue = "10000") Integer limit) {

        log.info("接收到查询订单请求，限制: {}", limit);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                List<Order> orders = largeDataService.getAllOrders(limit);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "查询订单成功");
                result.put("count", orders.size());
                result.put("limit", limit);
                result.put("data", orders);
                result.put("executionTime", executionTime + " ms");

                log.info("查询订单接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("查询订单失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "查询订单失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 查询所有用户
     */
    @GetMapping("/get-all-users")
    @Operation(summary = "查询所有用户", description = "查询数据库中的用户数据，限制最大返回10000条")
    public CompletableFuture<Map<String, Object>> getAllUsers(
            @Parameter(description = "返回记录数限制，默认10000，最大10000")
            @RequestParam(defaultValue = "10000") Integer limit) {

        log.info("接收到查询用户请求，限制: {}", limit);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                List<TestUser> users = largeDataService.getAllUsers(limit);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "查询用户成功");
                result.put("count", users.size());
                result.put("limit", limit);
                result.put("data", users);
                result.put("executionTime", executionTime + " ms");

                log.info("查询用户接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("查询用户失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "查询用户失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 根据用户ID查询订单（分页）
     */
    @GetMapping("/get-orders-by-user")
    @Operation(summary = "根据用户ID查询订单（分页）", description = "分页查询指定用户的订单数据")
    public CompletableFuture<Map<String, Object>> getOrdersByUserId(
            @Parameter(description = "用户ID", required = true)
            @RequestParam Long userId,
            @Parameter(description = "当前页，默认1")
            @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小，默认10")
            @RequestParam(defaultValue = "10") long size) {

        log.info("接收到查询用户订单请求，用户ID: {}, 当前页: {}, 每页: {}", userId, current, size);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                Page<Order> page =
                        largeDataService.getOrdersByUserId(userId, current, size);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "查询用户订单成功");
                result.put("userId", userId);
                result.put("current", page.getCurrent());
                result.put("size", page.getSize());
                result.put("total", page.getTotal());
                result.put("pages", page.getPages());
                result.put("records", page.getRecords());
                result.put("executionTime", executionTime + " ms");

                log.info("查询用户订单接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("查询用户订单失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "查询用户订单失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 分页查询订单
     */
    @GetMapping("/get-orders-by-page")
    @Operation(summary = "分页查询订单", description = "分页查询订单数据")
    public CompletableFuture<Map<String, Object>> getOrdersByPage(
            @Parameter(description = "当前页，默认1")
            @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小，默认10")
            @RequestParam(defaultValue = "10") long size) {

        log.info("接收到分页查询订单请求，当前页: {}, 每页: {}", current, size);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                Page<Order> page =
                        largeDataService.getOrdersByPage(current, size);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "分页查询订单成功");
                result.put("current", page.getCurrent());
                result.put("size", page.getSize());
                result.put("total", page.getTotal());
                result.put("pages", page.getPages());
                result.put("records", page.getRecords());
                result.put("executionTime", executionTime + " ms");

                log.info("分页查询订单接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("分页查询订单失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "分页查询订单失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 分页查询用户
     */
    @GetMapping("/get-users-by-page")
    @Operation(summary = "分页查询用户", description = "分页查询用户数据")
    public CompletableFuture<Map<String, Object>> getUsersByPage(
            @Parameter(description = "当前页，默认1")
            @RequestParam(defaultValue = "1") long current,
            @Parameter(description = "每页大小，默认10")
            @RequestParam(defaultValue = "10") long size) {

        log.info("接收到分页查询用户请求，当前页: {}, 每页: {}", current, size);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                Page<TestUser> page =
                        largeDataService.getUsersByPage(current, size);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "分页查询用户成功");
                result.put("current", page.getCurrent());
                result.put("size", page.getSize());
                result.put("total", page.getTotal());
                result.put("pages", page.getPages());
                result.put("records", page.getRecords());
                result.put("executionTime", executionTime + " ms");

                log.info("分页查询用户接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("分页查询用户失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "分页查询用户失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 清空所有订单
     */
    @GetMapping("/clear-orders")
    @Operation(summary = "清空所有订单", description = "删除数据库中的所有订单数据")
    public CompletableFuture<Map<String, Object>> clearAllOrders() {

        log.info("接收到清空所有订单请求");

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                int count = largeDataService.clearAllOrders();

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "清空订单成功");
                result.put("count", count);
                result.put("executionTime", executionTime + " ms");

                log.info("清空所有订单接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("清空所有订单失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "清空订单失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 清空所有用户
     */
    @GetMapping("/clear-users")
    @Operation(summary = "清空所有用户", description = "删除数据库中的所有用户数据")
    public CompletableFuture<Map<String, Object>> clearAllUsers() {

        log.info("接收到清空所有用户请求");

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                int count = largeDataService.clearAllUsers();

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "清空用户成功");
                result.put("count", count);
                result.put("executionTime", executionTime + " ms");

                log.info("清空所有用户接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("清空所有用户失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "清空用户失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 统计订单和用户数量
     */
    @GetMapping("/stats")
    @Operation(summary = "统计数据", description = "统计订单和用户的数量")
    public CompletableFuture<Map<String, Object>> getStatistics() {

        log.info("接收到统计数据请求");

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                long orderCount = largeDataService.countOrders();
                long userCount = largeDataService.countUsers();

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "统计数据获取成功");
                result.put("orderCount", orderCount);
                result.put("userCount", userCount);
                result.put("executionTime", executionTime + " ms");

                log.info("统计数据接口执行完成，耗时: {} ms", executionTime);
                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("统计数据失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "统计数据失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }

    /**
     * 性能对比测试：单条插入 vs 批量插入
     */
    @GetMapping("/performance-test")
    @Operation(summary = "性能对比测试", description = "对比单条插入和批量插入的性能差异")
    public CompletableFuture<Map<String, Object>> performanceTest(
            @Parameter(description = "测试数量，建议：100、500、1000")
            @RequestParam(defaultValue = "100") int count) {

        log.info("接收到性能对比测试请求，数量: {}", count);

        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();

            try {
                Map<String, Object> performanceResult = largeDataService.performanceTest(count);

                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;

                Map<String, Object> result = new HashMap<>();
                result.put("success", true);
                result.put("message", "性能对比测试完成");
                result.put("data", performanceResult);
                result.put("executionTime", executionTime + " ms");

                log.info("性能对比测试接口执行完成，耗时: {} ms", executionTime);

                // 添加并行模式提示
                Map<String, Object> data = (Map<String, Object>) result.get("data");
                if (data != null) {
                    data.put("parallelTip", "提示：批量插入支持并行模式（parallel=true），每个批次使用独立longTask线程，性能可再提升 2-3 倍");
                }

                return result;
            } catch (Exception e) {
                long endTime = System.currentTimeMillis();
                log.error("性能对比测试失败", e);

                Map<String, Object> result = new HashMap<>();
                result.put("success", false);
                result.put("message", "性能对比测试失败: " + e.getMessage());
                result.put("executionTime", (endTime - startTime) + " ms");

                return result;
            }
        }, longTaskExecutor);
    }
}
