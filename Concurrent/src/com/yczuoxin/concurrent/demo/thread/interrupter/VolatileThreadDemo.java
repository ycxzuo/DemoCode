package com.yczuoxin.concurrent.demo.thread.interrupter;

import java.util.concurrent.TimeUnit;

/**
 * volatile 让所有线程可以共享变量
 * 如 CPU 的内存屏障使用的 MIES
 * M：Modify
 * I：Invalid
 * E：Exclusive
 * S：Shared
 * 如果一个值被修改 M，则其他的高速缓存中的该变量变成 I
 * 如果是 E，则只有当前 CPU 能使用到该变量
 * JMM 中的 8 个原子操作 use -> load -> read -> lock(volatile/monitorenter) -> unlock -> write -> store -> assign
 */
public class VolatileThreadDemo {

    public volatile static boolean stop = false;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            int i = 0;
            while (!stop) {
                i++;
            }
            System.out.println(i);
        });
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        stop = true;
    }

}
