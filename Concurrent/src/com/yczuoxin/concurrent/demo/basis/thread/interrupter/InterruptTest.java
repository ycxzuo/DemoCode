package com.yczuoxin.concurrent.demo.basis.thread.interrupter;

import java.util.concurrent.TimeUnit;

public class InterruptTest {

    public static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                i++;
            }
        });
        thread.start();
        System.out.println(thread.isInterrupted());
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt(); // 将 interrupt 标识为 true
        System.out.println(thread.isInterrupted());
        System.out.println(i);
    }
}
