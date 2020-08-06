package com.yczuoxin.concurrent.demo.basis.future.completable;

import java.util.concurrent.*;

public class MyCompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //demo1();
        //demo2();
        demo3();
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
            //int i = 1/0;
            sleep(1);
            return "task1";
        });
        CompletableFuture<String> future1 = future.whenComplete((f, e) -> {
            sleep(1);
            print("执行第二个方法: " + Thread.currentThread().getName());
            print("第一个方法的返回值: " + f);
            print(e.getMessage());
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
        //print(future2.get());
        //print(future.get());
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
            e.printStackTrace();
        }
    }

    private static void print(String message) {
        System.out.println(message);
    }
}
