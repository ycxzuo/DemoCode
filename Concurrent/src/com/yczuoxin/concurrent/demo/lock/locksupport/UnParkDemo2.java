package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 根据条件判断去唤醒线程
 */
public class UnParkDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");

            // 判断线程是否被中断
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }

            System.out.println("child thread unpark!");
        });

        thread.start();

        TimeUnit.SECONDS.sleep(1);

        System.out.println("main thread begin unpark!");

        // 此情况下线程不会被唤醒
        // LockSupport.unpark(thread);

        // 此情况下线程才会被唤醒
        thread.interrupt();
    }
}
