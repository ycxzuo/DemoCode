package com.yczuoxin.concurrent.demo.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 公平锁
 * 公平锁表示线程获取锁的顺序是按照线程请求锁的时间早晚决定的，
 * 也就是最早请求锁的线程最先获取到锁.
 *
 * 公平锁会带来额外的性能开销
 */
public class PairLock {
    public static void main(String[] args) {
        ReentrantLock pairLock = new ReentrantLock(true);
    }
}
