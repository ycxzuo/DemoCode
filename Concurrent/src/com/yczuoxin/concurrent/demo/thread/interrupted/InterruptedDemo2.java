package com.yczuoxin.concurrent.demo.thread.interrupted;

/**
 * 调用 interrupted() 将中断标志清除了
 */
public class InterruptedDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            while (!Thread.currentThread().interrupted()) {

            }

            System.out.println("ThreadOne isInterrupted: " + Thread.currentThread().isInterrupted());
        });

        threadOne.start();

        threadOne.interrupt();

        threadOne.join();
        System.out.println("main thread is over!");
    }
}
