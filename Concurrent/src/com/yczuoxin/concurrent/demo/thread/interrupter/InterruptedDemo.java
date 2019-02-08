package com.yczuoxin.concurrent.demo.thread.interrupter;

public class InterruptedDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
            try {
                System.out.println("ThreadOne begin sleep for 2000 seconds");
                Thread.sleep(2000);
                System.out.println("ThreadOne awaking");
            } catch (InterruptedException e) {
                System.out.println("ThreadOne is interrupted while sleeping");
                return;
            }
            System.out.println("ThreadOne-leaving normally");
        });
        threadOne.start();

        // 确保子线程进入了休眠状态
        Thread.sleep(1000);

        // 打断子线程的休眠，让子线程从 sleep 函数返回
        threadOne.interrupt();

        // 等待线程执行完毕
        threadOne.join();

        System.out.println("main thread is over");
    }
}
