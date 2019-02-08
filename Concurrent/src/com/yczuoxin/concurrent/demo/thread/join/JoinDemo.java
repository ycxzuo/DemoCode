package com.yczuoxin.concurrent.demo.thread.join;

/**
 * join() 是一个无参无返回值的方法，它是 Thread 类的方法
 * 该方法是等方法加载完毕，如果方法没有执行完毕，会被阻塞
 */
public class JoinDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread threadOne = new Thread(() -> {
           try {
               Thread.sleep(1000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }

            System.out.println("threadOne over!");
        });

        Thread threadTwo = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("threadTwo over!");
        });

        // 启动线程
        threadOne.start();
        threadTwo.start();

        System.out.println("wait all child thread over!");

        // 等待线程结束，返回
        threadOne.join();
        threadTwo.join();

        System.out.println("all child thread over!");
    }
}
