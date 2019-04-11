package com.yczuoxin.concurrent.demo.basis.thread.yield;

/**
 * yield() 方法是 Thread 的一个静态方法，当一个线程调用 yield() 方法时，实际上是在暗示线程调度器当前线程请求让出自己
 * 的 CPU 使用，但是 CPU 可以无条件忽略这个暗示
 *
 * 当一个线程调用 yield() 方法时，当前线程会让出 CPU 使用权，然后处于就绪状态，线程调度器会从线程就绪队列里面获取一个
 * 线程优先级最高的线程，当然也有可能会调度到刚刚让出 CPU 的那个线程来获取 CPU 的执行权限
 */
public class YieldTest implements Runnable {

    // 创建并启动当前线程
    YieldTest() {
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        for (int i = 0; i < 5; i++) {
            // 当 i = 0 时，让出 CPU 的执行权，放弃时间片，进行下一轮调度
            if (i % 5 == 0) {
                System.out.println(Thread.currentThread() + "yield cpu...");

                // 当前线程让出 CPU 执行权，放弃时间片，进行下一轮调度
                // 如果注释改代码后，就可能出现交叉打印
                //Thread.yield();
            }
        }

        System.out.println(Thread.currentThread() + " is over!");
    }

    public static void main(String[] args) {
        new YieldTest();
        new YieldTest();
        new YieldTest();
    }
}
