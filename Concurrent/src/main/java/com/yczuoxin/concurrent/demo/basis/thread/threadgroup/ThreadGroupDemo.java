package com.yczuoxin.concurrent.demo.basis.thread.threadgroup;

public class ThreadGroupDemo {
    public static void main(String[] args) {
        ThreadGroup group = new ThreadGroup("Test Group");
        Thread thread1 = new Thread(group,ThreadGroupDemo::print,"thread1");
        Thread thread2 = new Thread(group,ThreadGroupDemo::print,"thread2");

        thread1.start();
        thread2.start();
    }

    private static void print(){
        System.out.printf("线程组：[%s]的线程：[%s] 执行了 \n",
                Thread.currentThread().getThreadGroup().getName(),
                Thread.currentThread().getName());
    }
}
