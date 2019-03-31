package com.yczuoxin.concurrent.demo.lock.aqs.reentrantreadwritelock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 为了满足读多写少的场景，ReentrantReadWriteLock 应运而生，采用读写分离的策略，允许多线程同时获取读锁
 *
 * 用 state 高 16 表示读锁 ReadLock（共享锁），也就是获取到读锁的次数
 * 用 state 低 16 表示写锁 WriteLock（可重入锁），也就是获取到写锁的线程可重入次数次数
 *
 * 读锁 ReadLock
 *
 *
 * 写锁 WriteLock
 *      如果当前没有线程获取到读锁和写锁，则当前线程可以获取到写锁然后返回
 *      如果当前已经有线程获取到读锁和写锁，则当前线程已经获取了该锁，再次获取只需把可重入次数 +1 然后直接返回
 *
 */
public class ReentrantReadWriteLockDemo {
    private static Map<String, Object> map = new HashMap<>();

    private static ReadWriteLock rwl = new ReentrantReadWriteLock();

    private static Lock r = rwl.readLock();
    private static Lock w = rwl.writeLock();

    private static volatile boolean isUpdate;

    public static void main(String[] args) {
        readWrite();
    }

    public static void readWrite() {
        r.lock(); // 为了保证isUpdate能够拿到最新的值
        if (!isUpdate) {
            r.unlock();
            w.lock();
            map.put("xxx", "xxx");
            r.lock();
            w.unlock();
        }

        Object obj = map.get("xxx");

        System.out.println(obj);
        r.unlock();
    }
}
