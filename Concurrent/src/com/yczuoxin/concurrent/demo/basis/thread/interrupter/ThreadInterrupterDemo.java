package com.yczuoxin.concurrent.demo.basis.thread.interrupter;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupterDemo {

    public static void main(String[] args) throws InterruptedException {
        //methodOne();
        Thread thread = new Thread(() -> {
            while (true) {
                try {
                    System.out.println("start:" + Thread.currentThread().isInterrupted());// 此时为 true
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("after:" + Thread.currentThread().isInterrupted());
            }
        });
        thread.start();
        thread.interrupt();
        TimeUnit.SECONDS.sleep(1);
    }

    private static void methodOne() throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if (interrupted) {
                    System.out.println("before:" + interrupted);
                    Thread.interrupted(); // 复位为 false
                    System.out.println("after:" + Thread.currentThread().isInterrupted());
                }
            }
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt(); // 设置为true
    }
}
