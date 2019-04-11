package com.yczuoxin.jvm.demo.classloader.init;

public class InitDemo6 {
    static class DeadLoopClass {
        static {
            if (true) {
                System.out.println(Thread.currentThread() + "init DeadLoopClass");
                while (true) {
                }
            }
        }
    }

    public static void main(String[] args) {
        Runnable script = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread() + "start");
                DeadLoopClass clazz = new DeadLoopClass();
                System.out.println(Thread.currentThread() + "run over");
            }
        };

        Thread thread1 = new Thread(script);
        Thread thread2 = new Thread(script);
        thread1.start();
        thread2.start();
    }
    // Thread[Thread-0,5,main]start
    // Thread[Thread-1,5,main]start
    // Thread[Thread-0,5,main]init DeadLoopClass
}
