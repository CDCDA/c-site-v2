package com.pw;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication(scanBasePackages = {"com.pw"}, exclude = {DataSourceAutoConfiguration.class})
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class PServerV3Application implements CommandLineRunner {

    private static final ThreadLocal<byte[]> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        SpringApplication.run(PServerV3Application.class, args);
        System.out.println("C-SITE V2 Started!");
    }

    @Override
    public void run(String... args) {
        System.out.println("Starting ThreadLocal OOM simulation...");
//		simulateThreadLocalOOM();
    }

    //模拟ThreadLocal OOM
//	private void simulateThreadLocalOOM() {
//		// 创建固定大小的线程池
//		ExecutorService executorService = Executors.newFixedThreadPool(10);
//
//		// 模拟大量任务执行，每个任务都向ThreadLocal中设置大对象
//		for (int i = 0; i < 100000; i++) {
//			final int taskId = i;
//			executorService.submit(() -> {
//				try {
//					// 在ThreadLocal中设置1MB的大对象
//					byte[] largeObject = new byte[1024 * 1024];
//					threadLocal.set(largeObject);
//
//					// 模拟业务处理
//					Thread.sleep(ThreadLocalRandom.current().nextInt(10, 100));
//
//					System.out.println("Task " + taskId + " processed in thread: " + Thread.currentThread().getName());
//
//					// 故意不调用remove()，模拟忘记清理ThreadLocal
//					// threadLocal.remove(); // 注释掉remove，模拟内存泄漏
//				} catch (InterruptedException e) {
//					Thread.currentThread().interrupt();
//				}
//			});
//
//			// 每执行10个任务打印一次内存使用情况
//			if (i % 10 == 0) {
//				printMemoryUsage();
//			}
//		}
//
//		// 关闭线程池
//		executorService.shutdown();
//		System.out.println("ThreadLocal OOM simulation completed!");
//	}

    private void printMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / (1024 * 1024);
        long totalMemory = runtime.totalMemory() / (1024 * 1024);
        long freeMemory = runtime.freeMemory() / (1024 * 1024);
        long usedMemory = totalMemory - freeMemory;

        System.out.printf("Memory Usage - Max: %dMB, Total: %dMB, Used: %dMB, Free: %dMB%n",
                maxMemory, totalMemory, usedMemory, freeMemory);
    }

}
