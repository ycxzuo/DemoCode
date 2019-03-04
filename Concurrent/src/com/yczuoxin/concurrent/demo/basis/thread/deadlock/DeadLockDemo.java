package com.yczuoxin.concurrent.demo.basis.thread.deadlock;

/**
 * 死锁的四个必备条件
 *  互斥条件
 *      线程对已经获取到的资源进行排他性使用，即该资源同时只能被一个线程占用
 *  请求并持有条件
 *      指一个线程已经持有了至少一个资源，但又提出了新的资源请求，而新资源已经被其他线程占有
 *  不可剥夺条件
 *      线程获取到的资源在自己使用完之前不能被其他线程抢占，只有自己使用完毕后才由自己释放
 *  环路等待条件
 *      发生死锁时，必然存在一个线程一资源的环形链
 */
public class DeadLockDemo {

    // 创建资源
    private static Object resourceA = new Object();
    private static Object resourceB = new Object();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            synchronized (resourceA) {
                System.out.println(Thread.currentThread() + " get resourceA");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread() + " waiting for resourceB");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + " get resourceB");
                }
            }
        });

        Thread threadB = new Thread(() -> {
            synchronized (resourceB) {
                System.out.println(Thread.currentThread() + " get resourceB");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread() + " waiting for resourceA");
                synchronized (resourceA) {
                    System.out.println(Thread.currentThread() + " get resourceA");
                }
            }
            // 解决方案
            /*synchronized (resourceA) {
                System.out.println(Thread.currentThread() + " get resourceB");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread() + " waiting for resourceA");
                synchronized (resourceB) {
                    System.out.println(Thread.currentThread() + " get resourceA");
                }
            }*/
        });

        threadA.start();
        threadB.start();
    }
}
