package com.yczuoxin.concurrent.demo.thread.interrupted;

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
