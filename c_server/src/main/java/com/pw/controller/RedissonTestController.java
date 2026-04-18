package com.pw.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.KSQLWindow;
import org.redisson.api.*;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * Redisson 功能测试控制器
 * 提供接口演示 Redisson 的各种分布式功能
 */
@Tag(name = "Redisson 测试", description = "Redisson 分布式功能测试接口")
@RestController
@RequestMapping("/api/redisson")
@RequiredArgsConstructor
@Slf4j
public class RedissonTestController {

    private final RedissonClient redissonClient;

    /**
     * 测试分布式锁
     */
    @Operation(summary = "测试分布式锁")
    @GetMapping("/lock")
    public Map<String, Object> testLock(@RequestParam(defaultValue = "test") String key) {
        Map<String, Object> result = new HashMap<>();

        RLock lock = redissonClient.getLock("lock:" + key);

        try {
            boolean isLocked = lock.tryLock(5, 10, java.util.concurrent.TimeUnit.SECONDS);
            result.put("locked", isLocked);

            if (isLocked) {
                result.put("message", "加锁成功");
                result.put("thread", Thread.currentThread().getName());

                // 模拟业务处理
                Thread.sleep(2000);
            } else {
                result.put("message", "加锁失败，业务繁忙");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            result.put("error", "加锁被中断");
        } finally {
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
            }
        }

        return result;
    }

    /**
     * 测试分布式 Map
     */
    @Operation(summary = "测试分布式 Map")
    @PostMapping("/map")
    public Map<String, Object> testMap(@RequestParam String key,
                                        @RequestParam String field,
                                        @RequestParam String value) {
        Map<String, Object> result = new HashMap<>();

        RMap<String, String> map = redissonClient.getMap("map:" + key);

        map.put(field, value);

        result.put("key", key);
        result.put("field", field);
        result.put("value", value);
        result.put("allData", map.readAllMap());
        result.put("size", map.size());

        return result;
    }

    /**
     * 测试分布式计数器
     */
    @Operation(summary = "测试分布式计数器")
    @GetMapping("/counter")
    public Map<String, Object> testCounter(@RequestParam String key,
                                            @RequestParam(defaultValue = "1") long delta) {
        Map<String, Object> result = new HashMap<>();

        RAtomicLong counter = redissonClient.getAtomicLong("counter:" + key);

        long newValue = counter.addAndGet(delta);

        result.put("key", key);
        result.put("delta", delta);
        result.put("newValue", newValue);

        return result;
    }

    /**
     * 测试限流器
     */
    @Operation(summary = "测试限流器")
    @GetMapping("/ratelimiter")
    public Map<String, Object> testRateLimiter(@RequestParam String key,
                                                 @RequestParam(defaultValue = "1") int permits) {
        Map<String, Object> result = new HashMap<>();

        RRateLimiter rateLimiter = redissonClient.getRateLimiter("ratelimiter:" + key);

        // 初始化限流器：每秒 10 个请求
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);

        boolean acquired = rateLimiter.tryAcquire(permits);

        result.put("key", key);
        result.put("permits", permits);
        result.put("acquired", acquired);
        result.put("message", acquired ? "获取许可成功" : "被限流");

        return result;
    }

    /**
     * 测试布隆过滤器
     */
    @Operation(summary = "测试布隆过滤器")
    @PostMapping("/bloomfilter")
    public Map<String, Object> testBloomFilter(@RequestParam String key,
                                                 @RequestParam String value,
                                                 @RequestParam(defaultValue = "add") String operation) {
        Map<String, Object> result = new HashMap<>();

        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("bloomfilter:" + key);

        // 初始化布隆过滤器
        if (!bloomFilter.isExists()) {
            bloomFilter.tryInit(10000, 0.01);
        }

        if ("add".equals(operation)) {
            bloomFilter.add(value);
            result.put("message", "元素已添加");
        } else if ("check".equals(operation)) {
            boolean contains = bloomFilter.contains(value);
            result.put("contains", contains);
            result.put("message", contains ? "元素可能存在" : "元素一定不存在");
        }

        result.put("key", key);
        result.put("value", value);
        result.put("operation", operation);

        return result;
    }

    /**
     * 测试分布式 Set
     */
    @Operation(summary = "测试分布式 Set")
    @PostMapping("/set")
    public Map<String, Object> testSet(@RequestParam String key,
                                         @RequestParam String value,
                                         @RequestParam(defaultValue = "add") String operation) {
        Map<String, Object> result = new HashMap<>();

        RSet<String> set = redissonClient.getSet("set:" + key);

        if ("add".equals(operation)) {
            boolean added = set.add(value);
            result.put("added", added);
            result.put("message", added ? "添加成功" : "元素已存在");
        } else if ("remove".equals(operation)) {
            boolean removed = set.remove(value);
            result.put("removed", removed);
            result.put("message", removed ? "删除成功" : "元素不存在");
        }

        result.put("key", key);
        result.put("value", value);
        result.put("size", set.size());
        result.put("allElements", set.readAll());

        return result;
    }

    /**
     * 测试分布式 List
     */
    @Operation(summary = "测试分布式 List")
    @PostMapping("/list")
    public Map<String, Object> testList(@RequestParam String key,
                                         @RequestParam String value,
                                         @RequestParam(defaultValue = "add") String operation) {
        Map<String, Object> result = new HashMap<>();

        RList<String> list = redissonClient.getList("list:" + key);

        if ("add".equals(operation)) {
            list.add(value);
            result.put("message", "添加成功");
        } else if ("remove".equals(operation)) {
            boolean removed = list.remove(value);
            result.put("removed", removed);
            result.put("message", removed ? "删除成功" : "元素不存在");
        }

        result.put("key", key);
        result.put("value", value);
        result.put("size", list.size());
        result.put("allElements", list.readAll());

        return result;
    }

    /**
     * 测试 Bucket
     */
    @Operation(summary = "测试 Bucket")
    @PostMapping("/bucket")
    public Map<String, Object> testBucket(@RequestParam String key,
                                            @RequestParam String value,
                                            @RequestParam(defaultValue = "600") long ttlSeconds) {
        Map<String, Object> result = new HashMap<>();

        RBucket<String> bucket = redissonClient.getBucket("bucket:" + key);

        bucket.set(value, Duration.ofDays(ttlSeconds));

        result.put("key", key);
        result.put("value", value);
        result.put("ttlSeconds", ttlSeconds);
        result.put("storedValue", bucket.get());
        result.put("exists", bucket.isExists());

        return result;
    }

    /**
     * 获取 Redisson 信息
     */
    @Operation(summary = "获取 Redisson 信息")
    @GetMapping("/info")
    public Map<String, Object> getInfo() {
        Map<String, Object> result = new HashMap<>();

        NodesGroup nodesGroup = redissonClient.getNodesGroup();
        result.put("nodes", nodesGroup.getNodes());
        result.put("ping", nodesGroup.pingAll());

        return result;
    }

    /**
     * 测试 HyperLogLog
     */
    @Operation(summary = "测试 HyperLogLog")
    @PostMapping("/hyperloglog")
    public Map<String, Object> testHyperLogLog(@RequestParam String key,
                                                 @RequestParam String value) {
        Map<String, Object> result = new HashMap<>();

        RHyperLogLog<String> hll = redissonClient.getHyperLogLog("hll:" + key);

        hll.add(value);
        long count = hll.count();

        result.put("key", key);
        result.put("value", value);
        result.put("uniqueCount", count);

        return result;
    }
}
