package com.yczuoxin.concurrent.demo.basis.thread.join;

public class JoInDemo2 {
    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            System.out.println("ThreadOne begin run!");
            for (;;) {

            }
        });

        final Thread mainThread = Thread.currentThread();

        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 调用的主线程的 interrupt() 方法
            mainThread.interrupt();
        });

        threadOne.start();
        threadTwo.start();

        try {
            threadOne.join();
        } catch (InterruptedException e) {
            System.out.println("main thread:" + e);
        }
    }

}
