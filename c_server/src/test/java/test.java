import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.KSQLWindow;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

@Slf4j
public class test {

    private static ThreadLocal<Integer> threadLocal = ThreadLocal.withInitial(() -> 0);

    // 定义一个密封抽象类 Shape，只允许 Circle、Rectangle、Triangle 继承
    public abstract sealed class Shape
            permits Circle, Rectangle {
        // 抽象方法
        public abstract double area();
    }

    public abstract class A {
        // 抽象方法
        public abstract double area();
    }


    // Circle 是 final，不能再有子类
    public final class Circle extends Shape {
        private final double radius;
        public Circle(double radius) { this.radius = radius; }
        @Override public double area() { return Math.PI * radius * radius; }
    }

    // Rectangle 是 non-sealed，允许任何人继承它
    public non-sealed class Rectangle extends Shape {
        private final double width, height;
        public Rectangle(double width, double height) { this.width = width; this.height = height; }
        @Override public double area() { return width * height; }
    }


    public static void main(String[] args) throws InterruptedException {
       List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
       log.info(list.getLast().toString());
    }

//    public static void testThreadPoolExecutor() throws InterruptedException {
//        try (ThreadPoolExecutor executor = new ThreadPoolExecutor(
//                2, 5, 60L, TimeUnit.SECONDS,
//                new LinkedBlockingQueue<>(10),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy()
//        )) {
//                executor.submit(() -> {
//                    int value = threadLocal.get(); // 获取当前线程的副本
//                    threadLocal.set(value + 1);
//                    System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
//                });
//                executor.submit(() -> {
//                    int value = threadLocal.get(); // 获取当前线程的副本
//                    threadLocal.set(value + 1);
//                    System.out.println(Thread.currentThread().getName() + ": " + threadLocal.get());
//                });
//        }
//    }

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void testDeadLock() {
        Thread t1 = new Thread(() -> {
           synchronized(lock1){
               log.info("线程1获取到锁1");
               try{
                   sleep(100);
               } catch (InterruptedException e) {
                   throw new RuntimeException(e);
               }
               synchronized(lock2){
                   log.info("线程1获取到锁2");
               }
           }
        });
        Thread t2 = new Thread(() -> {
            synchronized(lock2){
                log.info("线程2获取到锁2");
                try{
                    sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
//                synchronized(lock1){
//                    log.info("线程2获取到锁1");
//                }
            }
        });
        t1.start();
        t2.start();
    }

//     public static void testVirtualThread() {
//        try(ThreadPoolExecutor executor = Executors.newVirtualThreadPerTaskExecutor(
//                2, 5, 60L, TimeUnit.SECONDS,
//                new LinkedBlockingQueue<>(10),
//                Executors.defaultThreadFactory(),
//                new ThreadPoolExecutor.AbortPolicy()
//        )) {
//            executor.submit(() -> {
//                log.info("线程1");
//            });
//            executor.submit(() -> {
//                log.info("线程2");
//            });
//        }
//    }


//    public static void testExecutor() throws ExecutionException, InterruptedException {
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        executor.submit(() -> {
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            log.info("Hello:Executor1");
//
//        });
//        FutureTask<String> futureTask = new FutureTask<>(new Callable<String>() {
//            @Override
//            public String call() throws Exception {
//                return "Hello:FutureTask";
//            }
//        });
//        executor.submit(futureTask);
//        String futureTaskResult = futureTask.get();
//        log.info(futureTaskResult);
//        executor.submit(() -> {
//            try {
//                sleep(1000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//            log.info("Hello:Executor2");
//        });
//        executor.shutdown();
//    }

    public void testCallable() throws ExecutionException, InterruptedException {
        class myCallable implements Callable<String> {
            @Override
            public String call() throws Exception {
                return "Hello:Callable";
            }
        }
        FutureTask<String> futureTask = new FutureTask<>(new myCallable());
        Thread thread = new Thread(futureTask);
        thread.start();
        String result = futureTask.get();
        log.info(result);
    }
}
