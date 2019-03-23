package com.yczuoxin.concurrent.demo.lock.AQS.ReentrantLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁示例
 * AQS 的 state 状态值表示该线程获取该锁的可重入次数
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread threadOne = new Thread(() -> {
            try {
                System.out.println("threadOne begin lock");
                lock.lockInterruptibly();
                TimeUnit.SECONDS.sleep(1);
                System.out.println("threadOne begin lock second");
                lock.lockInterruptibly();
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.println("threadOne end lock");
        });

        threadOne.start();
    }
}
