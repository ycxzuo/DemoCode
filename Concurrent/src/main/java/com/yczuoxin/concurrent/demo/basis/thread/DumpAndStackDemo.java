package com.yczuoxin.concurrent.demo.basis.thread;

public class DumpAndStackDemo {
    public static void main(String[] args) {
        Thread thread = Thread.currentThread();
        StackTraceElement[] stackTrace = thread.getStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            System.out.println(i);
            System.out.println(stackTrace[i].toString());
        }
        System.out.println(thread.getContextClassLoader());
    }
}
