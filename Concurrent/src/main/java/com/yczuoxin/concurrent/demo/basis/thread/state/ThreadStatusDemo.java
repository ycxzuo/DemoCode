package com.yczuoxin.concurrent.demo.basis.thread.state;

import java.util.concurrent.TimeUnit;

/**
 *  Thread 有 6 种状态，详情见 Thread 的内部枚举 State（1742）
 *  NEW
 *  RUNNABLE
 *  BLOCKED
 *  WAITING
 *  TIMED_WAITING
 *  TERMINATED
 */
public class ThreadStatusDemo {

    public static void main(String[] args) {
        // TIMED_WAITING
        new Thread(() -> {
            try {
                Thread.sleep(100000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"TIME WAITING1").start();

        // TIMED_WAITING
        new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "TIME WAITING2").start();

        // WAITING
        new Thread(() -> {
            while (true) {
                synchronized (ThreadStatusDemo.class) {
                    try {
                        ThreadStatusDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "WAITING").start();

        // 以下有一个是BLOCKED,有一个是TIMED_WAITING
        new Thread(new BlockThread(),"Block-0").start();
        new Thread(new BlockThread(),"Block-1").start();
    }

    static class BlockThread extends Thread {
        @Override
        public void run() {
            while (true) {
                synchronized (BlockThread.class) {
                    try {
                        TimeUnit.SECONDS.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
