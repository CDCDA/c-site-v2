package com.pw;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Java 21 虚拟线程与普通线程性能对比测试
 * <p>
 * 运行方式：
 * 1. 在 IDE 中直接运行 main 方法
 * 2. 或使用命令：mvn test -Dtest=VirtualThreadBenchmark
 * <p>
 * JMH 参数说明：
 * - warmup: 预热迭代次数，让 JIT 编译器优化代码
 * - measurement: 正式测量迭代次数
 * - fork: 进程 fork 次数，避免 JVM 优化干扰
 * - batchSize: 每次迭代执行的操作次数
 */
@BenchmarkMode(Mode.Throughput) // 吞吐量模式：每秒操作数
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Benchmark)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class VirtualThreadBenchmark {

    // 测试任务数量
    @Param({"100", "1000", "10000"})
    public int taskCount;

    // 模拟阻塞时间（毫秒）
    @Param({"10"})
    public long blockTimeMs;

    // 虚拟线程执行器
    private ExecutorService virtualThreadExecutor;

    // 固定大小线程池
    private ExecutorService fixedThreadPool;

    // 缓存线程池
    private ExecutorService cachedThreadPool;

    // 平台线程池（与虚拟线程数量相同的平台线程）
    private ExecutorService platformThreadPool;

    @Setup(Level.Trial)
    public void setup() {
        virtualThreadExecutor = Executors.newVirtualThreadPerTaskExecutor();
        fixedThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        cachedThreadPool = Executors.newCachedThreadPool();
        platformThreadPool = Executors.newFixedThreadPool(taskCount);
    }

    @TearDown(Level.Trial)
    public void tearDown() throws InterruptedException {
        shutdownExecutor(virtualThreadExecutor);
        shutdownExecutor(fixedThreadPool);
        shutdownExecutor(cachedThreadPool);
        shutdownExecutor(platformThreadPool);
    }

    private void shutdownExecutor(ExecutorService executor) throws InterruptedException {
        executor.shutdown();
        if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
            executor.shutdownNow();
        }
    }

    /**
     * 简单计算任务 - 不涉及阻塞
     * 预期：平台线程池性能更好（虚拟线程有调度开销）
     */
//    @Benchmark
//    public void simpleCalculation_PlatformThread(Blackhole bh) throws Exception {
//        CountDownLatch latch = new CountDownLatch(taskCount);
//        AtomicLong result = new AtomicLong(0);
//
//        for (int i = 0; i < taskCount; i++) {
//            fixedThreadPool.submit(() -> {
//                // 简单计算
//                long sum = 0;
//                for (int j = 0; j < 1000; j++) {
//                    sum += j;
//                }
//                result.addAndGet(sum);
//                latch.countDown();
//            });
//        }
//
//        latch.await();
//        bh.consume(result.get());
//    }
//
//    @Benchmark
//    public void simpleCalculation_VirtualThread(Blackhole bh) throws Exception {
//        CountDownLatch latch = new CountDownLatch(taskCount);
//        AtomicLong result = new AtomicLong(0);
//
//        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int i = 0; i < taskCount; i++) {
//                executor.submit(() -> {
//                    // 简单计算
//                    long sum = 0;
//                    for (int j = 0; j < 1000; j++) {
//                        sum += j;
//                    }
//                    result.addAndGet(sum);
//                    latch.countDown();
//                });
//            }
//            latch.await();
//        }
//
//        bh.consume(result.get());
//    }

    /**
     * 模拟 I/O 阻塞操作
     * 预期：虚拟线程性能显著更好（阻塞时不占用 OS 线程）
     */
//    @Benchmark
//    public void blockingIO_PlatformThread_Fixed(Blackhole bh) throws Exception {
//        CountDownLatch latch = new CountDownLatch(taskCount);
//
//        for (int i = 0; i < taskCount; i++) {
//            fixedThreadPool.submit(() -> {
//                try {
//                    // 模拟 I/O 阻塞
//                    Thread.sleep(blockTimeMs);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        bh.consume(latch.getCount());
//    }
//
//    @Benchmark
//    public void blockingIO_PlatformThread_Cached(Blackhole bh) throws Exception {
//        CountDownLatch latch = new CountDownLatch(taskCount);
//
//        for (int i = 0; i < taskCount; i++) {
//            cachedThreadPool.submit(() -> {
//                try {
//                    Thread.sleep(blockTimeMs);
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        bh.consume(latch.getCount());
//    }
//
//    @Benchmark
//    public void blockingIO_VirtualThread(Blackhole bh) throws Exception {
//        CountDownLatch latch = new CountDownLatch(taskCount);
//
//        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
//            for (int i = 0; i < taskCount; i++) {
//                executor.submit(() -> {
//                    try {
//                        Thread.sleep(blockTimeMs);
//                    } catch (InterruptedException e) {
//                        Thread.currentThread().interrupt();
//                    } finally {
//                        latch.countDown();
//                    }
//                });
//            }
//            latch.await();
//        }
//
//        bh.consume(latch.getCount());
//    }
//
    /**
     * 混合场景：计算 + 阻塞
     */
    @Benchmark
    public void mixedWorkload_PlatformThread(Blackhole bh) throws Exception {
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicLong result = new AtomicLong(0);

        for (int i = 0; i < taskCount; i++) {
            fixedThreadPool.submit(() -> {
                try {
                    // 计算部分
                    long sum = 0;
                    for (int j = 0; j < 500; j++) {
                        sum += j;
                    }
                    result.addAndGet(sum);

                    // 阻塞部分
                    Thread.sleep(blockTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        bh.consume(result.get());
    }

    @Benchmark
    public void mixedWorkload_VirtualThread(Blackhole bh) throws Exception {
        CountDownLatch latch = new CountDownLatch(taskCount);
        AtomicLong result = new AtomicLong(0);

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < taskCount; i++) {
                executor.submit(() -> {
                    try {
                        // 计算部分
                        long sum = 0;
                        for (int j = 0; j < 500; j++) {
                            sum += j;
                        }
                        result.addAndGet(sum);

                        // 阻塞部分
                        Thread.sleep(blockTimeMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        bh.consume(result.get());
    }

    /**
     * 线程创建开销测试
     * 预期：虚拟线程创建开销极小
     */
    @Benchmark
    public void threadCreation_PlatformThread(Blackhole bh) throws Exception {
        Thread[] threads = new Thread[taskCount];
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            threads[i] = Thread.ofPlatform().unstarted(latch::countDown);
        }

        for (Thread t : threads) {
            t.start();
        }

        latch.await();
        bh.consume(threads.length);
    }

    @Benchmark
    public void threadCreation_VirtualThread(Blackhole bh) throws Exception {
        Thread[] threads = new Thread[taskCount];
        CountDownLatch latch = new CountDownLatch(taskCount);

        for (int i = 0; i < taskCount; i++) {
            threads[i] = Thread.ofVirtual().unstarted(latch::countDown);
        }

        for (Thread t : threads) {
            t.start();
        }

        latch.await();
        bh.consume(threads.length);
    }

    /**
     * JMH 主入口
     */
    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }
}
