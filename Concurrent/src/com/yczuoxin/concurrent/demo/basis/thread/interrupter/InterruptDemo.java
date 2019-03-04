package com.yczuoxin.concurrent.demo.basis.thread.interrupter;

/**
 * 可能线程需要等待 2 秒，但是由于某些特殊原因不需要等待那么久了，
 * 此时可以使用 interrupt() 方法抛出异常而返回，线程恢复到激活状态
 */
public class InterruptDemo {

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
