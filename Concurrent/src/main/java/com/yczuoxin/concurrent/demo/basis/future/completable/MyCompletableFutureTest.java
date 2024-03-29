package com.yczuoxin.concurrent.demo.basis.future.completable;

import java.io.Serializable;
import java.util.concurrent.*;

/**
 * CompletableFuture 与 Stream 并行流的区别：
 *  completableFuture 可以自定义线程池
 *  Stream 使用的默认的 ForkJoinPool，核心线程数跟CPU核心数有关，不能自定义
 */
public class MyCompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //demo1();
        //demo2();
        demo3();
        //demo4();
        //demo5();
    }

    /**
     * 任务 1 和任务 2 是不同的线程执行
     * 在 combine 的时候可能是 main 线程，也可能是任务 1 的线程执行
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void demo5() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            print("执行第一个方法: " + Thread.currentThread().getName());
            //int i = 1/0;
            return "task1";
        });
        CompletableFuture<String> future1 = future.thenCombine(CompletableFuture.supplyAsync(() -> {
            print("执行第二个方法: " + Thread.currentThread().getName());
            return "task2";
        }), (result1, result2) -> {
            print("执行第三个方法: " + Thread.currentThread().getName());
            return result1 + " and " + result2;
        });
        print(future1.get());
    }

    /**
     * 一定会用第一个线程去跑第二个任务
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void demo4() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            print("执行第一个方法: " + Thread.currentThread().getName());
            print("执行第一个方法是否是守护线程: " + Thread.currentThread().isDaemon());
            //int i = 1/0;
            sleep(1);
            return "task1";
        });
        CompletableFuture<String> future1 = future.thenCompose(result -> CompletableFuture.supplyAsync(() -> {
            print("执行第二个方法");
            print("使用线程: " + Thread.currentThread().getName());
            print("执行第二个方法是否是守护线程: " + Thread.currentThread().isDaemon());
            return result + " and task2";
        }));
        print(future1.get());
    }

    /**
     * 第二个方法可能是 main 线程执行或者 forkJoinPool 的线程执行
     * 如果第一个任务线程执行完了，main 线程还没有走到第二个任务，则会由 main 线程执行
     * 否则会有执行第一个任务的线程执行
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void demo3() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            print("执行第一个方法: " + Thread.currentThread().getName());
            print("执行第一个方法是否是守护线程: " + Thread.currentThread().isDaemon());
            //int i = 1/0;
            sleep(3);
            return "task1";
        });
        CompletableFuture<String> future1 = future.whenComplete((f, e) -> {
            sleep(1);
            print("执行第二个方法: " + Thread.currentThread().getName());
            print("执行第二个方法是否是守护线程: " + Thread.currentThread().isDaemon());
            print("第一个方法的返回值: " + f);
            print(e);
        });
        print("main");
        print(future1.get());
    }

    /**
     * 默认使用的线程是 forkJoinPool 获取的，并且是守护线程
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private static void demo2() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            print("executor service 是否是守护线程 :" + Thread.currentThread().isDaemon());
            return null;
        });
        CompletableFuture<String> future= CompletableFuture.supplyAsync(() -> {
            print("this is lambda supplyAsync");
            print("supplyAsync lambda 是否是守护线程 :" + Thread.currentThread().isDaemon());
            print("supplyAsync lambda Thread Name :" + Thread.currentThread().getName() + ", " + Thread.currentThread().getThreadGroup().getName());
            sleep(2);
            print("this lambda is executed by forkJoinPool");
            return "result1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            print("this is executor service supplyAsync");
            print("supplyAsync executor service 是否是守护线程 :" + Thread.currentThread().isDaemon());
            print("supplyAsync executor service Thread Name :" + Thread.currentThread().getName() + ", " + Thread.currentThread().getThreadGroup().getName());
            return "result2";
        }, executorService);
        CompletableFuture<Void> future3 = CompletableFuture.allOf(future, future2);
        future3.get();
        print(future2.get());
        print(future.get());
        executorService.shutdown();
    }

    private static void demo1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            print(Thread.currentThread().getName() + ": CompletableFuture 可以监控这个任务的执行");
            future.complete("任务返回结果");
        }).start();
        print(future.get());
    }


    private static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {

        }
    }

    private static void print(Serializable message) {
        System.out.println(message);
    }
}
