package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * park() 方法
 *
 * 调用 park() 方法的线程已经拿到了与 LockSupport 关联的许可证，则调用 LockSupport.park() 时会马上返回，
 * 否则调用线程会被禁止参与线程的调度，也就是会被阻塞挂起
 */
public class ParkDemo {
    public static void main(String[] args) {
        System.out.println("begin park!");

        Thread thread = Thread.currentThread();

        // 另起一个线程去唤醒此线程，不然此线程会一直被挂起
        Thread threadOne = new Thread(() -> {
            sleep(1L);

            // 设置中断标志是阻塞线程被唤醒，但是不会抛出 InterruptedException
            thread.interrupt();

            // 调用 unpark 方法唤醒主线程
            LockSupport.unpark(thread);
        });

        // 启动线程
        threadOne.start();
        LockSupport.park();

        System.out.println("end park!");
    }

    /**
     * 睡眠方法
     * @param second 睡眠时间
     */
    private static void sleep(Long second) {
        try {
            TimeUnit.SECONDS.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
