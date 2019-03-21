package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * 当一个线程调用 unpark 时，如果参数 thread 线程没有持有 thread 与 LockSupport 类关联的许可证，
 * 则让 thread 线程持有，如果 thread 之前没有调用 park() 方法，则调用 unpark() 方法后，在调用 park()
 * 方法，其会立刻返回
 */
public class UnParkDemo {
    public static void main(String[] args) {
        System.out.println("begin park!");

        // 先调用 unpark() 方法
        LockSupport.unpark(Thread.currentThread());

        // 后调用 park() 方法
        LockSupport.park();

        System.out.println("end park!");
    }
}
