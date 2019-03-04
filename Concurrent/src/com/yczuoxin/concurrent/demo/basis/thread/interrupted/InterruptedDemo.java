package com.yczuoxin.concurrent.demo.basis.thread.interrupted;

/**
 * interrupted()  static 方法
 *      判断当前线程是否被中断，如果是，返回 true，否则返回 false，并清除中断标志
 * isInterrupted()
 *      判断当前线程是否被中断，如果是，返回 true，否则返回 false
 *
 * threadOne.interrupted() 与 Thread.interrupted() 方法作用是一样的，目的都是获取当前线程的中断标志
 *
 */
public class InterruptedDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            for (;;) {

            }
        });
        threadOne.start();

        // 设置中断标志
        threadOne.interrupt();

        // 获取中断标志
        System.out.println("isInterrupted: " + threadOne.isInterrupted());

        // 获取中断标志并重置
        System.out.println("isInterrupted: " + threadOne.interrupted());

        // 获取中断标志并重置
        System.out.println("isInterrupted: " + Thread.interrupted());

        // 获取中断标志
        System.out.println("isInterrupted: " + threadOne.isInterrupted());

        threadOne.join();

        System.out.println("main thread is over");
    }
}
