package com.yczuoxin.concurrent.demo.basis.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 非公平锁
 * 在锁资源被释放时进行对锁资源的争夺
 */
public class UnPairLock {
    public static void main(String[] args) {
        ReentrantLock unPairLock = new ReentrantLock(false);
    }
}
