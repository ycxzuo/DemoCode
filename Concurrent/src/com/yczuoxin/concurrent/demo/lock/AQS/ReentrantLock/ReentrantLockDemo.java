package com.yczuoxin.concurrent.demo.lock.AQS.ReentrantLock;

/**
 * 可重入锁示例
 * AQS 的 state 状态值表示该线程获取该锁的可重入次数，
 * 线程获取锁时先判断 state 值是否为 0，如果为 0，则表示该对象没有被其他线程占有，就可以获取到锁，
 * 如果不为 0，就会判断获得当前锁的线程是否为当前线程
 *      如果是当前线程，则利用 CAS 方式将 state + 1，并获取到锁
 *      如果不是当前线程，则表示该锁已经被其他线程占有，就会加入到 AQS 队列，等待 state 值变为 0，被唤醒
 *
 * 线程 threadOne 跑完才会跑 threadTwo，因为重入锁
 */
public class ReentrantLockDemo {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLockList list = new ReentrantLockList();
        Thread threadOne = new Thread(() -> {
            list.set("4",2);
            System.out.println("threadOne end");
        }, "threadOne");

        Thread threadTwo = new Thread(() -> {
            list.set("4",2);
            System.out.println("threadTwo end");
        }, "threadTwo");

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();
    }

}
