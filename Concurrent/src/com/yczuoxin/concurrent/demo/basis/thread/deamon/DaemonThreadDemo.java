package com.yczuoxin.concurrent.demo.basis.thread.deamon;

/**
 * Java 中线程分为两类
 *  守护线程(daemon thread)
 *  用户线程(user thread)
 * 只要有用户线程还没有结束，正常情况下 JVM 就不会退出
 * Tomcat 的接受线程和处理线程都是守护线程
 */
public class DaemonThreadDemo {
    public static void main(String[] args) {
        Thread daemonThread = new Thread(() -> {

        });

        // 设置该线程为守护线程
        daemonThread.setDaemon(true);
        daemonThread.start();
    }
}
