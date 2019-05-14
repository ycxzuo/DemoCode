package com.yczuoxin.concurrent.demo.basis.thread.start;

public class StartDemo {
    public static void main(String[] args) {
        MyThread thread = new MyThread();
        thread.start();


        MyRunnable runnable = new MyRunnable();
        Thread thread1 = new Thread(runnable);
        thread1.start();

        System.out.println("mian is finish");
    }
}
