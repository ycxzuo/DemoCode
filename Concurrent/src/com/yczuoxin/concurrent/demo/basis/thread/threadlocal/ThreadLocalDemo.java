package com.yczuoxin.concurrent.demo.basis.thread.threadlocal;

/**
 * ThreadLocal 实质上就是一个 map，他有一个自己的内部类 ThreadLocalMap，
 * map 中的元素 entry 都是软引用，
 *  key 为当前实例的 ThreadLocal 实例的引用，
 *  value 为需要存储的值
 *
 * Thread 类中有一个 ThreadLocals 变量，类型是 ThreadLocal.ThreadLocalMap，
 * 每个线程的本地变量不是存放在 ThreadLocal 中，而是存放在调用线程的 ThreadLocals 变量里
 */
public class ThreadLocalDemo {

    // 创建 ThreadLocal 变量
    static ThreadLocal<String> localVariable = new ThreadLocal<>();

    static void print(String str) {
        // 打印当前线程本地内存中 localVariable 变量的值
        System.out.println(str + ": " + localVariable.get());
        // 删除当前线程本地内存中 localVariable 变量的值方法
        // localVariable.remove();
    }

    public static void main(String[] args) {
        Thread threadOne = new Thread(() -> {
            localVariable.set("threadOne local variable");
            print("threadOne");
            System.out.println("threadOne remove after:" + localVariable.get());
        });

        Thread threadTwo = new Thread(() -> {
            localVariable.set("threadTwo local variable");
            print("threadTwo");
            System.out.println("threadTwo remove after:" + localVariable.get());
        });

        threadOne.start();
        threadTwo.start();
    }
}
