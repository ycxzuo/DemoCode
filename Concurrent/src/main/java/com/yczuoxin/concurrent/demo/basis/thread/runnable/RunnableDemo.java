package com.yczuoxin.concurrent.demo.basis.thread.runnable;

public class RunnableDemo {
    public static void main(String[] args) {
        Runnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable, "threadOne");
        Thread thread2 = new Thread(runnable, "threadTwo");

        thread1.start();
        thread2.start();
    }

    static class MyRunnable implements Runnable {
        // runnable 如果只实例化一次，那么局部变量所有线程可以共享
        int count = 1;

        @Override
        public void run() {
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "执行：" + count++);
                try {
                    Thread.sleep((int) Math.random() * 10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
