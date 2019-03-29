package com.yczuoxin.concurrent.demo.basis.thread.notify;

public class NotifyDemo2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(NotifyDemo2::sayHelloWorld);
        t1.setName("T1");
        t1.start(); // 启动线程

        Thread t2 = new Thread(NotifyDemo2::sayHelloWorld);
        t2.setName("T2");
        t2.start();

        Object monitor = NotifyDemo2.class;

        synchronized (monitor) {
            monitor.notify();
            System.out.println("------------------------------------");
            // monitor.notifyAll();
        }
    }

    public static void sayHelloWorld() {

        Thread currentThread = Thread.currentThread();

        Object monitor = NotifyDemo2.class;

        synchronized (monitor) {
            try {
                System.out.printf("线程[%s] 进入等待状态...\n", currentThread.getName());
                monitor.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("线程[%s] 恢复执行...\n", currentThread.getName());
            System.out.printf("线程[%s] : Hello,World!\n", currentThread.getName());
        }
    }
}
