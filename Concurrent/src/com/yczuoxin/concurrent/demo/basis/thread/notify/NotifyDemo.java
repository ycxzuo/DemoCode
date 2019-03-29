package com.yczuoxin.concurrent.demo.basis.thread.notify;

/**
 * notify
 *  一个线程调用共享对象的 notify() 方法后，会唤醒一个在该共享变量上调用 wait() 系列方法后挂起的线程
 *  一个共享变量可能会有多个线程在等待，具体唤醒哪个线程是随机的 (Hotspot 是 FIFO)，所以线程 A 一定先唤醒
 *
 * notifyAll
 *  notifyAll 会唤醒该共享变量上调用 wait() 系列方法的所有线程
 *
 */
public class NotifyDemo {
    private static volatile Object resourceA = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadA get resourceA lock");
                try {
                    System.out.println("threadA begin wait");
                    resourceA.wait();
                    System.out.println("threadA eng wait");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadB get resourceA lock");
                try {
                    System.out.println("threadB begin wait");
                    resourceA.wait();
                    System.out.println("threadB eng wait");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Thread threadC = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println("threadC begin notify");
                // 唤醒所有被阻塞的线程
                //resourceA.notifyAll();
                // 唤醒被阻塞的一个线程
                resourceA.notify();
            }
        });

        threadA.start();
        threadB.start();
        Thread.sleep(1000);

        threadC.start();

        // 等待线程结束
        threadA.join();
        threadB.join();
        threadC.join();

        System.out.println("main end");
    }
}
