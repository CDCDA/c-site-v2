package com.pw;

import com.pw.PServerV3Application;
import lombok.extern.slf4j.Slf4j;
import org.apache.calcite.runtime.Resources;
import org.junit.jupiter.api.Test;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Redisson 功能测试类
 * 演示 Redisson 的各种分布式功能
 */
@SpringBootTest(classes = PServerV3Application.class)
@Slf4j
public class RedissonTest {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 测试分布式锁（RLock）
     * 用于在分布式环境下实现互斥访问
     */
    @Test
    void testDistributedLock() {
        String lockKey = "lock:order:123";

        // 获取可重入锁
        RLock lock = redissonClient.getLock(lockKey);
        try {
            // 尝试加锁，最多等待 10 秒，锁 30 秒后自动释放
            boolean isLocked = lock.tryLock(10, 30, TimeUnit.SECONDS);

            if (isLocked) {
                log.info("加锁成功，执行业务逻辑...");

                // 模拟业务处理
                Thread.sleep(2000);

                log.info("业务处理完成");
            } else {
                log.info("加锁失败，业务繁忙");
            }
        } catch (InterruptedException e) {
            log.error("加锁被中断", e);
            Thread.currentThread().interrupt();
        } finally {
            // 释放锁
            if (lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.info("锁已释放");
            }
        }
    }

    /**
     * 测试分布式锁 - 多线程并发测试
     */
    @Test
    void testDistributedLockConcurrent() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadNum = i;
            executor.submit(() -> {
                RLock lock = redissonClient.getLock("lock:concurrent:test");
                try {
                    boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
                    if (isLocked) {
                        log.info("线程 {} 获取到锁", threadNum);
                        Thread.sleep(1000);  // 模拟业务处理
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        log.info("并发测试完成");
    }

    /**
     * 测试分布式读写锁（ReadWriteLock）
     * 读锁之间不互斥，写锁与所有锁互斥
     */
    @Test
    void testReadWriteLock() {
        String lockKey = "rwlock:config";

        RReadWriteLock rwLock = redissonClient.getReadWriteLock(lockKey);

        // 读锁测试
        RLock readLock = rwLock.readLock();
        try {
            readLock.lock();
            log.info("获取到读锁，读取配置...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            readLock.unlock();
        }

        // 写锁测试
        RLock writeLock = rwLock.writeLock();
        try {
            writeLock.lock();
            log.info("获取到写锁，写入配置...");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 测试分布式限流器（RRateLimiter）
     * 用于限流控制
     */
    @Test
    void testRateLimiter() {
        String rateLimiterKey = "ratelimiter:api:user";

        // 创建限流器：每秒 10 个请求
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(rateLimiterKey);
        rateLimiter.trySetRate(RateType.OVERALL, 10, 1, RateIntervalUnit.SECONDS);

        for (int i = 0; i < 20; i++) {
            // 尝试获取 1 个许可
            boolean acquired = rateLimiter.tryAcquire(1);
            log.info("请求 {}: {}", i, acquired ? "成功" : "被限流");
        }
    }

    /**
     * 测试分布式 Map（RMap）
     */
    @Test
    void testMap() {
        String mapKey = "map:user:info";

        // 获取 Map
        RMap<String, Object> map = redissonClient.getMap(mapKey);

        // 添加数据
        map.put("name", "张三");
        map.put("age", 25);
        map.put("email", "zhangsan@example.com");

        // 获取数据
        String name = (String) map.get("name");
        log.info("用户名: {}", name);

        // 获取所有数据
        Map<String, Object> allData = map.readAllMap();
        log.info("所有数据: {}", allData);

        // 删除数据
        map.delete();
    }

    /**
     * 测试分布式 Set（RSet）
     */
    @Test
    void testSet() {
        String setKey = "set:tags:article";

        RSet<String> set = redissonClient.getSet(setKey);

        // 添加元素
        set.add("Java");
        set.add("Spring Boot");
        set.add("Redis");
        set.add("Java");  // 重复元素不会被添加

        // 获取集合大小
        log.info("集合大小: {}", set.size());

        // 判断元素是否存在
        boolean contains = set.contains("Java");
        log.info("是否包含 Java: {}", contains);

        // 获取所有元素
        Collection<String> elements = set.readAll();
        log.info("所有元素: {}", elements);

        // 删除集合
        set.delete();
    }

    /**
     * 测试分布式 List（RList）
     */
    @Test
    void testList() {
        String listKey = "list:messages";

        RList<String> list = redissonClient.getList(listKey);

        // 添加元素
        list.add("消息1");
        list.add("消息2");
        list.add("消息3");

        // 获取列表大小
        log.info("列表大小: {}", list.size());

        // 获取指定索引的元素
        String message = list.get(0);
        log.info("第一条消息: {}", message);

        // 获取所有元素
        Collection<String> allMessages = list.readAll();
        log.info("所有消息: {}", allMessages);

        // 删除列表
        list.delete();
    }

    /**
     * 测试分布式计数器（RAtomicLong）
     */
    @Test
    void testAtomicLong() {
        String counterKey = "counter:page:view";

        RAtomicLong counter = redissonClient.getAtomicLong(counterKey);

        // 重置计数器
        counter.set(0);

        // 增加计数
        long value = counter.incrementAndGet();
        log.info("当前计数: {}", value);

        // 增加指定值
        value = counter.addAndGet(10);
        log.info("增加10后的计数: {}", value);

        // 减少计数
        value = counter.decrementAndGet();
        log.info("减少1后的计数: {}", value);

        // 设置过期时间
        counter.expire(1, TimeUnit.HOURS);

        // 删除计数器
        counter.delete();
    }

    /**
     * 测试布隆过滤器（RBloomFilter）
     * 用于大数据量下的快速去重和判断元素是否存在
     */
    @Test
    void testBloomFilter() {
        String bloomFilterKey = "bloomfilter:user:email";

        // 创建布隆过滤器：预期元素数量 10000，误判率 0.01
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter(bloomFilterKey);
        bloomFilter.tryInit(10000, 0.01);

        // 添加元素
        bloomFilter.add("user1@example.com");
        bloomFilter.add("user2@example.com");
        bloomFilter.add("user3@example.com");

        // 判断元素是否存在
        boolean exists1 = bloomFilter.contains("user1@example.com");
        boolean exists2 = bloomFilter.contains("user2@example.com");
        boolean exists3 = bloomFilter.contains("user4@example.com");

        log.info("user1@example.com 存在: {}", exists1);
        log.info("user2@example.com 存在: {}", exists2);
        log.info("user4@example.com 存在: {}", exists3);

        // 删除布隆过滤器
        bloomFilter.delete();
    }

    /**
     * 测试延迟队列（RDelayedQueue）
     * 用于实现延迟任务
     */
    @Test
    void testDelayedQueue() throws InterruptedException {
        String queueName = "delayedqueue:order";

        // 创建阻塞队列
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(queueName);

        // 创建延迟队列
        RDelayedQueue<String> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);

        // 添加延迟任务：5 秒后执行
        delayedQueue.offer("订单1需要处理", 5, TimeUnit.SECONDS);
        delayedQueue.offer("订单2需要处理", 3, TimeUnit.SECONDS);
        delayedQueue.offer("订单3需要处理", 1, TimeUnit.SECONDS);

        log.info("延迟任务已添加");

        // 从队列中获取任务（会阻塞直到有任务到期）
        for (int i = 0; i < 3; i++) {
            String task = blockingQueue.take();
            log.info("处理任务: {}", task);
        }

        // 销毁延迟队列
        delayedQueue.destroy();
    }

    /**
     * 测试分布式缓存（RMapCache）
     * 带有过期时间的 Map
     */
    @Test
    void testMapCache() throws InterruptedException {
        String cacheKey = "mapcache:session";

        RMapCache<String, String> mapCache = redissonClient.getMapCache(cacheKey);

        // 添加数据，设置过期时间为 5 秒
        mapCache.put("user:1", "张三", 5, TimeUnit.SECONDS);
        mapCache.put("user:2", "李四", 5, TimeUnit.SECONDS);

        // 获取数据
        String user1 = mapCache.get("user:1");
        log.info("用户1: {}", user1);

        // 等待 6 秒后再次获取
        Thread.sleep(6000);
        user1 = mapCache.get("user:1");
        log.info("6秒后用户1: {}", user1);  // 应该为 null

        // 删除缓存
        mapCache.delete();
    }

    /**
     * 测试分布式对象（RBucket）
     */
    @Test
    void testBucket() {
        String bucketKey = "bucket:config";

        RBucket<String> bucket = redissonClient.getBucket(bucketKey);

        // 设置值
        bucket.set("应用配置数据");

        // 设置带过期时间的值
        bucket.set("临时配置数据", 10, TimeUnit.SECONDS);

        // 获取值
        String value = bucket.get();
        log.info("Bucket 值: {}", value);

        // 检查是否存在
        boolean exists = bucket.isExists();
        log.info("是否存在: {}", exists);

        // 删除
        bucket.delete();
    }

    /**
     * 测试 HyperLogLog（用于基数统计）
     * 适合统计大量数据的去重计数（如 UV 统计）
     */
//    @Test
//    void testHyperLogLog() {
//        String hllKey = "hll:page:view";
//
//        RHyperLogLog<String> hyperLogLog = redissonClient.getHyperLogLog(hllKey);
//
//        // 添加元素
//        hyperLogLog.add("user1", "user2", "user3", "user1", "user2", "user4");
//
//        // 获取基数（去重后的数量）
//        long count = hyperLogLog.count();
//        log.info("独立访客数: {}", count);
//
//        // 合并多个 HyperLogLog
//        RHyperLogLog<String> hll2 = redissonClient.getHyperLogLog("hll:page:view:day2");
//        hll2.add("user3", "user4", "user5", "user6");
//
//        hyperLogLog.merge(hll2);
//        count = hyperLogLog.count();
//        log.info("合并后独立访客数: {}", count);
//
//        // 删除
//        hyperLogLog.delete();
//        hll2.delete();
//    }

    /**
     * 测试分布式 Semaphore
     * 用于限制并发访问数量
     */
    @Test
    void testSemaphore() {
        String semaphoreKey = "semaphore:resource:pool";

        // 创建信号量，最多允许 3 个并发
        RSemaphore semaphore = redissonClient.getSemaphore(semaphoreKey);

        // 设置许可数量
        semaphore.trySetPermits(3);

        try {
            // 获取许可
            semaphore.acquire(1);
            log.info("获取到许可，使用资源...");

            // 模拟使用资源
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // 释放许可
            semaphore.release(1);
            log.info("释放许可");
        }

        // 删除
        semaphore.delete();
    }

    /**
     * 测试分布式 CountDownLatch
     * 用于协调多个线程同步
     */
    @Test
    void testCountDownLatch() throws InterruptedException {
        String latchKey = "countdownlatch:startup";

        // 创建 CountDownLatch，需要等待 3 个线程完成
        RCountDownLatch latch = redissonClient.getCountDownLatch(latchKey);

        // 设置等待数量
        latch.trySetCount(3);

        // 在另一个线程中模拟任务完成
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                log.info("任务1完成");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(2000);
                log.info("任务2完成");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(3000);
                log.info("任务3完成");
                latch.countDown();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        // 主线程等待
        latch.await();
        log.info("所有任务已完成");

        // 删除
        latch.delete();
    }

    /**
     * 测试公平锁（Fair Lock）
     * 按照请求顺序获取锁，避免饥饿
     */
    @Test
    void testFairLock() {
        String lockKey = "fairlock:queue";

        RLock fairLock = redissonClient.getFairLock(lockKey);

        try {
            fairLock.lock();
            log.info("获取到公平锁，执行任务...");

            Thread.sleep(2000);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            fairLock.unlock();
            log.info("释放公平锁");
        }
    }

    /**
     * 测试红锁（RedLock）
     * 用于高可用的分布式锁，需要多个 Redis 节点
     * 这里使用单个节点演示，生产环境需要多个独立节点
     */
    @Test
    void testRedLock() {
        String lockKey = "redlock:critical";

        // 创建多个锁（实际使用时应该是不同的 Redis 节点）
        RLock lock1 = redissonClient.getLock(lockKey + ":1");
        RLock lock2 = redissonClient.getLock(lockKey + ":2");
        RLock lock3 = redissonClient.getLock(lockKey + ":3");

        // 创建红锁
        RedissonRedLock redLock = new RedissonRedLock(lock1, lock2, lock3);

        try {
            boolean locked = redLock.tryLock(10, 30, TimeUnit.SECONDS);
            if (locked) {
                log.info("获取到红锁，执行关键任务...");

                Thread.sleep(2000);

            } else {
                log.info("获取红锁失败");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            if (redLock.isHeldByCurrentThread()) {
                redLock.unlock();
                log.info("释放红锁");
            }
        }
    }
}
