package com.yczuoxin.concurrent.demo.basis.thread.sleep;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * sleep() 方法是 Thread 类中的一个静态方法，当一个执行中的线程调用了 Thread 的 sleep() 方法后
 * 调用的线程会暂时让出指定时间的执行权，也就是在这期间不参与 CPU 的调度，但是该线程所拥有的监视器资源，
 * 比如锁还是持有不让出的。指定的睡眠时间到了后该函数会正常返回，线程就处于就绪状态，然后参与 CPU 调度
 *
 * 如上代码首先创建了一个独占锁，然后创建了两个线程，每个线程在内部先获取锁，然后睡眠，睡眠结束后会释放锁
 * 线程 A 在睡眠的 10s 内还是持有者独占锁 lock，线程 B 会一直阻塞着直到线程 A 醒来后执行 unlock() 释放锁
 */
public class SleepTest {
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread threadA = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("child threadA is in sleep");

                Thread.sleep(10000);

                System.out.println("child threadA is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        Thread threadB = new Thread(() -> {
            lock.lock();
            try {
                System.out.println("child threadB is in sleep");

                Thread.sleep(10000);

                System.out.println("child threadB is in awaked");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        });

        threadA.start();
        threadB.start();
    }
}
