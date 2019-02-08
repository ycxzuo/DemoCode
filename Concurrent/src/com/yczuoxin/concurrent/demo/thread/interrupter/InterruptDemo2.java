package com.yczuoxin.concurrent.demo.thread.interrupter;

/**
 * 该方法通过线程的中断标志来控制是否退出循环
 */
public class InterruptDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread() + " hello");
            }
        });

        thread.start();

        Thread.sleep(1000);

        System.out.println("main thread interrupt thread");
        thread.interrupt();

        thread.join();
        System.out.println("main is over");
    }
}
