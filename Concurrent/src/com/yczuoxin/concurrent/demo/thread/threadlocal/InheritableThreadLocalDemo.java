package com.yczuoxin.concurrent.demo.thread.threadlocal;

/**
 * InheritableThreadLocal 类是一个可以让子线程获取父线程变量的类，存储在 Thread.inheritableThreadLocals 中。
 * 在调用 Thread 的 init() 初始化方法的时候回去看当前线程中 inheritableThreadLocals 是不是为 null，如果不是 null，
 * 则会将其传递给子线程(即即将初始化的线程)
 */
public class InheritableThreadLocalDemo {
    // 创建线程变量可以被子线程使用的变量
    public static ThreadLocal<String> threadLocal = new InheritableThreadLocal<>();
    // 创建线程变量
    //public static ThreadLocal<String> threadLocal = new ThreadLocal<>();

    public static void main(String[] args) {
        threadLocal.set("hello world");

        Thread thread = new Thread(() -> {
            // 子线程输出线程变量的值
            System.out.println("thread: " + threadLocal.get());
        });
        thread.start();

        // 主线程输出线程变量的值
        System.out.println("main:" + threadLocal.get());
    }
}
