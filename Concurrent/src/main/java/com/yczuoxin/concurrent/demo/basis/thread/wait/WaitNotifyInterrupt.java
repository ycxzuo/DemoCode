package com.yczuoxin.concurrent.demo.basis.thread.wait;

/**
 * 当一个线程调用一个共享变量的 wait() 方法时，该调用线程会被阻塞挂起，知道发生下面几件事情之一才返回
 *  1. 其他线程调用了该共享对象的 notify() 或者 notifyAll() 方法
 *  2. 其他方法调用了该线程的 interrupt() 方法，该线程抛出InterruptedException异常返回
 *
 * 如果调用 wait() 方法的线程没有事先获取该对象的监视器锁，调用 wait() 方法时调用线程会抛出 IllegalMonitorStateException 异常
 */
public class WaitNotifyInterrupt {
    static Object obj = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            try {
                System.out.println("---begin---");
                synchronized (obj) {
                    obj.wait();
                }
                System.out.println("---end---");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        threadA.start();

        Thread.sleep(1000);

        System.out.println("---begin interrupt ThreadA---");
        threadA.interrupt();
        System.out.println("---end interrupt ThreadA---");
    }
}
