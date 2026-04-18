package com.pw.service;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pw.domain.Order;
import com.pw.domain.TestUser;
import com.pw.mapper.OrderMapper;
import com.pw.mapper.TestUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 大批量数据测试服务
 *
 * @author cyd
 * @date 2026/04/07
 * @description 提供大批量数据的增删改查测试功能，使用MyBatis-Plus批量操作优化性能
 */
@Slf4j
@Service
public class LargeDataService extends ServiceImpl<OrderMapper, Order> {
    private static final Snowflake SNOW_FLAKE = IdUtil.getSnowflake(1, 0);
    private final OrderMapper orderMapper;
    private final TestUserMapper testUserMapper;

    // 订单Service（用于批量操作）
    private final com.pw.service.impl.OrderServiceImpl orderService;
    private final com.pw.service.impl.TestUserServiceImpl userService;

    // 长任务线程池（用于并行处理批次）
    private final Executor longTaskExecutor;

    // 事务模板（用于编程式事务管理）
    private final TransactionTemplate transactionTemplate;

    public LargeDataService(
            OrderMapper orderMapper,
            TestUserMapper testUserMapper,
            com.pw.service.impl.OrderServiceImpl orderService,
            com.pw.service.impl.TestUserServiceImpl userService,
            @Qualifier("longTaskExecutor") Executor longTaskExecutor,
            TransactionTemplate transactionTemplate) {
        this.orderMapper = orderMapper;
        this.testUserMapper = testUserMapper;
        this.orderService = orderService;
        this.userService = userService;
        this.longTaskExecutor = longTaskExecutor;
        this.transactionTemplate = transactionTemplate;
    }

    private static final String PRODUCT_NAMES[] = {
            "iPhone 15 Pro", "MacBook Pro", "iPad Air", "AirPods Pro",
            "Apple Watch", "iMac", "Mac mini", "Magic Keyboard",
            "Magic Mouse", "HomePod", "iPhone 15", "iPhone 15 Plus",
            "MacBook Air", "iPad Pro", "AirPods", "Magic Trackpad"
    };

    /**
     * 每批处理的数据量
     */
    private static final int BATCH_SIZE = 1000;

    /**
     * 最大并行批次数（控制并发线程数）
     */
    private static final int MAX_PARALLEL_BATCHES = 15;

    /**
     * 批量插入订单数据（使用MyBatis-Plus saveBatch优化，支持并行处理）
     *
     * @param count    插入数量
     * @param parallel 是否使用并行处理，默认true
     * @return 插入的订单数量
     */
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    /**
     * 批量插入订单数据（按 user_id 分片并行，避免死锁）
     *
     * @param count   插入数量
     * @param parallel 是否使用并行（若为 false 则退化为串行单事务）
     * @return 插入的订单数量
     */
    public long batchInsertOrders(int count, boolean parallel) {
        log.info("开始批量插入 {} 条订单数据，并行模式: {}", count, parallel);
        long startTime = System.currentTimeMillis();

        // 分片数量（建议等于或略小于线程池最大并行数）
        int shardCount = parallel ? MAX_PARALLEL_BATCHES : 1;
        // 构建分片数据
        List<List<Order>> shardedOrders = buildShardedOrders(count, shardCount);
        long totalInserted;

        if (parallel && shardCount > 1) {
            totalInserted = processShardsParallel(shardedOrders);
        } else {
            // 串行单事务
            totalInserted = processShardsSequential(shardedOrders);
        }

        long endTime = System.currentTimeMillis();
        log.info("批量插入 {} 条订单数据完成，实际插入 {} 条，总耗时: {} ms，平均: {} ms/条",
                count, totalInserted, (endTime - startTime), (endTime - startTime) * 1.0 / count);
        return totalInserted;
    }

    /**
     * 串行处理所有分片（单个大事务）
     */
    private Integer processShardsSequential(List<List<Order>> shards) {
        // 合并所有分片订单
        List<Order> allOrders = shards.stream().flatMap(List::stream).collect(Collectors.toList());
        boolean success = orderService.saveBatch(allOrders);
        return success ? allOrders.size() : 0;
    }

    /**
     * 并行处理各分片（每个分片一个独立事务）
     */
    private long processShardsParallel(List<List<Order>> shards) {
        int shardCount = shards.size();
        log.info("使用分片并行模式，分片数: {}", shardCount);

        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (int i = 0; i < shardCount; i++) {
            final int shardIndex = i;
            final List<Order> shardOrders = shards.get(i);
            if (shardOrders.isEmpty()) {
                continue;
            }

            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() ->
            {
                long start = System.currentTimeMillis();
                int insertedCount = batchInsertWithinTransaction(shardOrders);
                long end = System.currentTimeMillis();
                log.info("分片 [{}] 处理完成，插入 {} 条，耗时 {} ms", shardIndex, insertedCount, (end - start));
                return insertedCount;
            }, longTaskExecutor);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        return futures.stream().mapToInt(f -> {
            try {
                return f.get();
            } catch (Exception e) {
                log.error("获取分片结果失败", e);
                return 0;
            }
        }).sum();
    }

    /**
     * 在单个事务内批量插入一个分片的所有订单（内部可再分批）
     */
    private int batchInsertWithinTransaction(List<Order> orders) {
        if (orders.isEmpty()) return 0;
        int total = 0;
        // 将一个分片的数据再按 BATCH_SIZE 拆分成多个小批次，但仍在一个事务内
        for (int i = 0; i < orders.size(); i += BATCH_SIZE) {
            long startTime = System.currentTimeMillis();
            int end = Math.min(i + BATCH_SIZE, orders.size());
            List<Order> batch = orders.subList(i, end);
            boolean success = orderService.saveBatch(batch);
            if (success) total += batch.size();
            long endTime = System.currentTimeMillis();
            log.info("小分片 [{}] 处理完成，插入 {} 条，耗时 {} ms", i, batch.size(), (endTime - startTime));
        }
        return total;
    }


    /**
     * 构建分片订单数据
     *
     * @param totalCount 总订单数
     * @param shardCount 分片数量
     * @return 每个分片的订单列表
     */
    private List<List<Order>> buildShardedOrders(int totalCount, int shardCount) {
        List<List<Order>> shards = new ArrayList<>(shardCount);
        for (int i = 0; i < shardCount; i++) {
            shards.add(new ArrayList<>());
        }

        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        for (int i = 0; i < totalCount; i++) {
            Order order = new Order();
            // 使用雪花算法生成分片键 id，确保分布式唯一性
            long id = SNOW_FLAKE.nextId();
            order.setId(id);
            // 生成 user_id 范围 1~10000，确保分布均匀
            long userId = (long) (random.nextInt(10000) + 1);
            order.setOrderNo("ORD" + id + String.format("%04d", random.nextInt(10000)));
            order.setUserId(userId);
            order.setProductName(PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)]);
            order.setAmount(BigDecimal.valueOf(random.nextDouble() * 10000 + 100));
            order.setStatus(random.nextInt(5));
            if (order.getStatus() >= 1 && order.getStatus() <= 3) {
                order.setPayTime(new Date(System.currentTimeMillis() - random.nextInt(86400000)));
            }
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setRemark("测试订单数据");

            // 根据 id（分片键）取模分配分片，与ShardingSphere分片策略一致
            int shardIdx = (int) (id % shardCount);
            shards.get(shardIdx).add(order);
        }

        log.info("分片构建完成，各分片数据量: {}", shards.stream().map(List::size).collect(Collectors.toList()));
        return shards;
    }


    /**
     * 批量插入用户数据（使用MyBatis-Plus saveBatch优化，支持并行处理）
     *
     * @param count    插入数量
     * @param parallel 是否使用并行处理，默认true
     * @return 插入的用户数量
     */
    public long batchInsertUsers(int count, boolean parallel) {
        log.info("开始批量插入 {} 条用户数据（{}saveBatch优化，{}并行处理）",
                count, parallel ? "并行+" : "", parallel ? "启用" : "禁用");

        long startTime = System.currentTimeMillis();
        long totalInserted = 0;

        // 预先构建所有批次的数据
        List<List<TestUser>> allBatches = buildUserBatches(count);
        int totalBatches = allBatches.size();

        log.info("已构建 {} 个批次，准备{}处理", totalBatches, parallel ? "并行" : "顺序");

        if (parallel && totalBatches > 1) {
            // 并行处理批次
            totalInserted = processUserBatchesParallel(allBatches);
        } else {
            // 顺序处理批次
            totalInserted = processUserBatchesSequential(allBatches, count);
        }

        long endTime = System.currentTimeMillis();
        log.info("批量插入 {} 条用户数据完成，实际插入 {} 条，总耗时: {} ms，平均: {} ms/条",
                count, totalInserted, (endTime - startTime), (endTime - startTime) * 1.0 / count);

        return totalInserted;
    }

    /**
     * 并行处理用户批次
     */
    private long processUserBatchesParallel(List<List<TestUser>> allBatches) {
        int totalBatches = allBatches.size();
        int maxParallel = Math.min(MAX_PARALLEL_BATCHES, totalBatches);

        log.info("使用并行模式，最大并行批次: {}", maxParallel);

        List<CompletableFuture<Integer>> futures = new ArrayList<>();

        for (int i = 0; i < totalBatches; i++) {
            final int batchIndex = i;
            final List<TestUser> batch = allBatches.get(i);

            // 使用 TransactionTemplate 管理每个批次的事务
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                long batchStartTime = System.currentTimeMillis();
                boolean success = userService.saveBatch(batch);
                long batchEndTime = System.currentTimeMillis();

                int insertedCount = success ? batch.size() : 0;

                log.info("用户批次 [{}/{}] 处理完成，插入 {} 条，耗时: {} ms",
                        batchIndex + 1, totalBatches, insertedCount, batchEndTime - batchStartTime);

                return insertedCount;
            }, longTaskExecutor);

            futures.add(future);
        }

        // 等待所有批次完成
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        // 汇总结果
        long totalInserted = futures.stream()
                .mapToLong(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        log.error("获取用户批次结果失败", e);
                        return 0;
                    }
                })
                .sum();

        return totalInserted;
    }

    /**
     * 顺序处理用户批次
     */
    private long processUserBatchesSequential(List<List<TestUser>> allBatches, int totalCount) {
        long totalInserted = 0;
        int totalBatches = allBatches.size();

        for (int i = 0; i < totalBatches; i++) {
            List<TestUser> batch = allBatches.get(i);

            long batchStartTime = System.currentTimeMillis();
            boolean success = userService.saveBatch(batch);
            long batchEndTime = System.currentTimeMillis();

            if (success) {
                totalInserted += batch.size();
            }

            // 每处理5000条输出一次进度
            int processedSoFar = (i + 1) * BATCH_SIZE;
            if (processedSoFar % 5000 == 0 || i == totalBatches - 1) {
                log.info("已处理 {}/{} 条用户数据（批次 [{}/{}]）",
                        processedSoFar, totalCount, i + 1, totalBatches);
            }

            log.debug("用户批次 [{}/{}] 耗时: {} ms", i + 1, totalBatches, batchEndTime - batchStartTime);
        }

        return totalInserted;
    }

    /**
     * 构建所有用户批次的数据
     */
    private List<List<TestUser>> buildUserBatches(int count) {
        Random random = new Random();
        List<List<TestUser>> allBatches = new ArrayList<>();

        int processed = 0;
        while (processed < count) {
            int batchSize = Math.min(BATCH_SIZE, count - processed);
            List<TestUser> batch = new ArrayList<>(batchSize);

            for (int i = 0; i < batchSize; i++) {
                TestUser user = new TestUser();
                user.setUserName("testuser_" + System.currentTimeMillis() + "_" + (processed + i));
                user.setEmail(user.getUserName() + "@example.com");

                StringBuilder phone = new StringBuilder("13");
                for (int j = 0; j < 9; j++) {
                    phone.append(random.nextInt(10));
                }
                user.setPhone(phone.toString());
                user.setRegisterTime(new Date());

                batch.add(user);
            }

            allBatches.add(batch);
            processed += batchSize;
        }

        return allBatches;
    }

    /**
     * 查询所有订单（限制最大返回数量）
     *
     * @param limit 限制返回数量，默认10000
     * @return 订单列表
     */
    public List<Order> getAllOrders(Integer limit) {
        long startTime = System.currentTimeMillis();

        if (limit == null || limit > 10000) {
            limit = 10000;
        }

        log.info("开始查询订单数据，最大返回: {} 条", limit);

        // 使用分页查询
        Page<Order> page = new Page<>(1, limit);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<Order> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Order::getId);

        page = orderMapper.selectPage(page, queryWrapper);
        List<Order> orders = page.getRecords();

        long endTime = System.currentTimeMillis();
        log.info("查询订单数据完成，返回 {} 条，总记录数: {}，耗时: {} ms",
                orders.size(), page.getTotal(), (endTime - startTime));

        return orders;
    }

    /**
     * 查询所有用户（限制最大返回数量）
     *
     * @param limit 限制返回数量，默认10000
     * @return 用户列表
     */
    public List<TestUser> getAllUsers(Integer limit) {
        long startTime = System.currentTimeMillis();

        if (limit == null || limit > 10000) {
            limit = 10000;
        }

        log.info("开始查询用户数据，最大返回: {} 条", limit);

        Page<TestUser> page = new Page<>(1, limit);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TestUser> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(TestUser::getId);

        page = testUserMapper.selectPage(page, queryWrapper);
        List<TestUser> users = page.getRecords();

        long endTime = System.currentTimeMillis();
        log.info("查询用户数据完成，返回 {} 条，总记录数: {}，耗时: {} ms",
                users.size(), page.getTotal(), (endTime - startTime));

        return users;
    }

    /**
     * 根据用户ID查询订单（分页）
     *
     * @param userId  用户ID
     * @param current 当前页
     * @param size    每页大小
     * @return 订单分页结果
     */
    public Page<Order> getOrdersByUserId(Long userId, long current, long size) {
        long startTime = System.currentTimeMillis();
        log.info("开始分页查询用户 {} 的订单数据，第 {} 页，每页 {} 条", userId, current, size);

        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.eq(Order::getUserId, userId);
        queryWrapper.orderByDesc(Order::getId);

        page = orderMapper.selectPage(page, queryWrapper);

        long endTime = System.currentTimeMillis();
        log.info("分页查询用户 {} 的订单数据完成，返回 {} 条，总记录数: {}，耗时: {} ms",
                userId, page.getRecords().size(), page.getTotal(), (endTime - startTime));

        return page;
    }

    /**
     * 分页查询订单
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 订单分页结果
     */
    public Page<Order> getOrdersByPage(long current, long size) {
        long startTime = System.currentTimeMillis();
        log.info("开始分页查询订单数据，第 {} 页，每页 {} 条", current, size);

        Page<Order> page = new Page<>(current, size);
        LambdaQueryWrapper<Order> queryWrapper =
                new LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(Order::getId);

        page = orderMapper.selectPage(page, queryWrapper);

        long endTime = System.currentTimeMillis();
        log.info("分页查询订单数据完成，返回 {} 条，总记录数: {}，耗时: {} ms",
                page.getRecords().size(), page.getTotal(), (endTime - startTime));

        return page;
    }

    /**
     * 分页查询用户
     *
     * @param current 当前页
     * @param size    每页大小
     * @return 用户分页结果
     */
    public Page<TestUser> getUsersByPage(long current, long size) {
        long startTime = System.currentTimeMillis();
        log.info("开始分页查询用户数据，第 {} 页，每页 {} 条", current, size);

        Page<TestUser> page = new Page<>(current, size);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<TestUser> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        queryWrapper.orderByDesc(TestUser::getId);

        page = testUserMapper.selectPage(page, queryWrapper);

        long endTime = System.currentTimeMillis();
        log.info("分页查询用户数据完成，返回 {} 条，总记录数: {}，耗时: {} ms",
                page.getRecords().size(), page.getTotal(), (endTime - startTime));

        return page;
    }

    /**
     * 批量删除订单（使用MyBatis-Plus removeBatchByIds优化）
     *
     * @param ids 订单ID列表
     * @return 删除数量
     */
    @Transactional(rollbackFor = Exception.class)
    public long batchDeleteOrders(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        long startTime = System.currentTimeMillis();
        log.info("开始批量删除 {} 条订单数据（分批处理）", ids.size());

        long totalDeleted = 0;
        int processed = 0;

        // 分批删除
        while (processed < ids.size()) {
            int end = Math.min(processed + BATCH_SIZE, ids.size());
            List<Long> batchIds = ids.subList(processed, end);

            boolean success = orderService.removeBatchByIds(batchIds);
            if (success) {
                totalDeleted += batchIds.size();
            }

            processed = end;

            if (processed % 5000 == 0) {
                log.info("已删除 {}/{} 条订单数据", processed, ids.size());
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("批量删除订单数据完成，实际删除 {} 条，总耗时: {} ms", totalDeleted, (endTime - startTime));

        return totalDeleted;
    }

    /**
     * 批量删除用户（使用MyBatis-Plus removeBatchByIds优化）
     *
     * @param ids 用户ID列表
     * @return 删除数量
     */
    @Transactional(rollbackFor = Exception.class)
    public long batchDeleteUsers(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        long startTime = System.currentTimeMillis();
        log.info("开始批量删除 {} 条用户数据（分批处理）", ids.size());

        long totalDeleted = 0;
        int processed = 0;

        while (processed < ids.size()) {
            int end = Math.min(processed + BATCH_SIZE, ids.size());
            List<Long> batchIds = ids.subList(processed, end);

            boolean success = userService.removeBatchByIds(batchIds);
            if (success) {
                totalDeleted += batchIds.size();
            }

            processed = end;

            if (processed % 5000 == 0) {
                log.info("已删除 {}/{} 条用户数据", processed, ids.size());
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("批量删除用户数据完成，实际删除 {} 条，总耗时: {} ms", totalDeleted, (endTime - startTime));

        return totalDeleted;
    }

    /**
     * 清空所有订单（使用TRUNCATE TABLE，速度更快）
     *
     * @return 删除数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int clearAllOrders() {
        long startTime = System.currentTimeMillis();
        log.info("开始清空所有订单数据");

        // 先统计数量
        Long count = orderMapper.selectCount(null);

        // 使用TRUNCATE TABLE清空表（比DELETE快很多，但不支持事务回滚）
        // 这里使用delete(null)以确保事务安全
        int deletedCount = orderMapper.delete(null);

        long endTime = System.currentTimeMillis();
        log.info("清空 {} 条订单数据完成，耗时: {} ms", count, (endTime - startTime));

        return deletedCount;
    }

    /**
     * 清空所有用户
     *
     * @return 删除数量
     */
    @Transactional(rollbackFor = Exception.class)
    public int clearAllUsers() {
        long startTime = System.currentTimeMillis();
        log.info("开始清空所有用户数据");

        // 先统计数量
        Long count = testUserMapper.selectCount(null);

        int deletedCount = testUserMapper.delete(null);

        long endTime = System.currentTimeMillis();
        log.info("清空 {} 条用户数据完成，耗时: {} ms", count, (endTime - startTime));

        return deletedCount;
    }

    /**
     * 统计订单数量
     *
     * @return 订单总数
     */
    public long countOrders() {
        long startTime = System.currentTimeMillis();
        log.info("开始统计订单数量");

        Long count = orderMapper.selectCount(null);

        long endTime = System.currentTimeMillis();
        log.info("统计订单数量完成：{} 条，耗时: {} ms", count, (endTime - startTime));

        return count;
    }

    /**
     * 统计用户数量
     *
     * @return 用户总数
     */
    public long countUsers() {
        long startTime = System.currentTimeMillis();
        log.info("开始统计用户数量");

        Long count = testUserMapper.selectCount(null);

        long endTime = System.currentTimeMillis();
        log.info("统计用户数量完成：{} 条，耗时: {} ms", count, (endTime - startTime));

        return count;
    }

    /**
     * 性能对比测试：单条插入 vs 批量插入
     *
     * @param count 测试数量
     * @return 性能对比结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> performanceTest(int count) {
        log.info("开始性能对比测试，测试数量: {}", count);

        Map<String, Object> result = new HashMap<>();
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        // 测试1：单条插入
        long startTime1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            order.setOrderNo("SINGLE" + sdf.format(new Date()) + String.format("%04d", random.nextInt(10000)));
            order.setUserId((long) (random.nextInt(10000) + 1));
            order.setProductName(PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)]);
            order.setAmount(BigDecimal.valueOf(random.nextDouble() * 10000 + 100));
            order.setStatus(random.nextInt(5));
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setRemark("性能测试-单条插入");
            orderMapper.insert(order);
        }
        long endTime1 = System.currentTimeMillis();
        long singleInsertTime = endTime1 - startTime1;

        // 测试2：批量插入
        long startTime2 = System.currentTimeMillis();
        List<Order> batchList = new ArrayList<>(BATCH_SIZE);
        for (int i = 0; i < count; i++) {
            Order order = new Order();
            order.setOrderNo("BATCH" + sdf.format(new Date()) + String.format("%04d", random.nextInt(10000)));
            order.setUserId((long) (random.nextInt(10000) + 1));
            order.setProductName(PRODUCT_NAMES[random.nextInt(PRODUCT_NAMES.length)]);
            order.setAmount(BigDecimal.valueOf(random.nextDouble() * 10000 + 100));
            order.setStatus(random.nextInt(5));
            order.setCreateTime(new Date());
            order.setUpdateTime(new Date());
            order.setRemark("性能测试-批量插入");
            batchList.add(order);
        }
        orderService.saveBatch(batchList);
        long endTime2 = System.currentTimeMillis();
        long batchInsertTime = endTime2 - startTime2;

        result.put("testCount", count);
        result.put("singleInsertTime", singleInsertTime + " ms");
        result.put("singleInsertAvgTime", String.format("%.2f", singleInsertTime * 1.0 / count) + " ms/条");
        result.put("batchInsertTime", batchInsertTime + " ms");
        result.put("batchInsertAvgTime", String.format("%.2f", batchInsertTime * 1.0 / count) + " ms/条");
        result.put("speedup", String.format("%.2f", singleInsertTime * 1.0 / batchInsertTime) + "x");
        result.put("timeSaved", String.format("%.2f", (singleInsertTime - batchInsertTime) * 100.0 / singleInsertTime) + "%");

        log.info("性能对比测试完成，单条插入: {} ms，批量插入: {} ms，加速: {}x",
                singleInsertTime, batchInsertTime, singleInsertTime * 1.0 / batchInsertTime);

        return result;
    }
}
