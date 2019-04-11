package com.yczuoxin.concurrent.demo.basis.thread.sleep;

/**
 * 子线程在睡眠期间，主线程中断了它，所以子线程在调用 sleep() 方发处抛出了 interruptedException 异常
 */
public class SleepTest2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                System.out.println("child thread is in sleep");

                Thread.sleep(10000);
                System.out.println("child thread is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        thread.start();

        Thread.sleep(2000);
        thread.interrupt();
    }
}
