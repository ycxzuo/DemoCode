package com.yczuoxin.concurrent.demo.basis.future.completable;

import java.util.concurrent.*;

public class MyCompletableFutureTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //demo1();
        //demo2();
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("执行第一个方法");
            sleep(1);
            return "task1";
        });
        future.whenComplete((f, e) -> {
            sleep(1);
            System.out.println("第一个方法的返回值: " + f);
            System.out.println(e);
        });
    }

    private static void demo2() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(() -> {
            System.out.println("executor service 是否是守护线程 :" + Thread.currentThread().isDaemon());
            return null;
        });
        CompletableFuture<String> future= CompletableFuture.supplyAsync(() -> {
            System.out.println("this is lambda supplyAsync");
            System.out.println("supplyAsync lambda 是否是守护线程 :" + Thread.currentThread().isDaemon());
            System.out.println("supplyAsync lambda Thread Name :" + Thread.currentThread().getName() + ", " + Thread.currentThread().getThreadGroup().getName());
            sleep(2);
            System.out.println("this lambda is executed by forkJoinPool");
            return "result1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("this is executor service supplyAsync");
            System.out.println("supplyAsync executor service 是否是守护线程 :" + Thread.currentThread().isDaemon());
            System.out.println("supplyAsync executor service Thread Name :" + Thread.currentThread().getName() + ", " + Thread.currentThread().getThreadGroup().getName());
            return "result2";
        }, executorService);
        CompletableFuture<Void> future3 = CompletableFuture.allOf(future, future2);
        future3.get();
        //System.out.println(future2.get());
        //System.out.println(future.get());
        executorService.shutdown();
    }

    private static void demo1() throws InterruptedException, ExecutionException {
        CompletableFuture<String> future = new CompletableFuture<>();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + ": CompletableFuture 可以监控这个任务的执行");
            future.complete("任务返回结果");
        }).start();
        System.out.println(future.get());
    }


    private static void sleep(int i) {
        try {
            TimeUnit.SECONDS.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
