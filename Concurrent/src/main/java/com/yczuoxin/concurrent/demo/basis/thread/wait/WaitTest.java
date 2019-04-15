package com.yczuoxin.concurrent.demo.basis.thread.wait;

/**
 * wait() 方法调用是阻塞当前线程而不是使用的对象
 */
public class WaitTest {

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            synchronized (WaitTest.class) {
                try {
                    Thread.sleep(1000); //  使当前线阻塞 1s，确保主程序的 t1.wait(); 执行之后再执行 notify()
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+" call notify()");
                // 唤醒当前的wait线程
                WaitTest.class.notify();
            }
        },"t1");
        synchronized(t1) {
            try {
                // 启动线程 t1
                System.out.println(Thread.currentThread().getName()+" start t1");
                t1.start();
                // 主线程等待 t1 通过 notify() 唤醒
                System.out.println(Thread.currentThread().getName()+" wait()");
                t1.wait();  //  不是使 t1 线程等待，而是当前执行 wait() 的线程等待
                System.out.println(Thread.currentThread().getName()+" continue");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
