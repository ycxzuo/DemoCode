package com.yczuoxin.concurrent.demo.threadlocalrandom.localrandom;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 继承了 Random 类并重写了 nextInt() 方法，其没有用继承自 Random 的原子性种子变量，
 * 具体的种子存放在调用线程的 threadLocalRandomSeed 变量里，ThreadLocalRandom 类似 ThreadLocal，
 * 就是个工具类，在线程调用 ThreadLocalRandom 的 current() 方法时，ThreadLocalRandom 负责初始化
 * 调用线程的 threadLocalRandomSeed 变量，也就是初始化种子
 *
 * java.security.SecureRandom.getSeed(8)
 *
 * 初始化调用的 SecureRandom 的方法
 *
 * 如果 probeGenerator 值为 0，则表示当前线程第一次调用 ThreadLocalRandom 的 current() 方法，
 * 那么就需要调用 localInit() 初始化种子变量，这是一种延迟加载的优化方法
 *
 * nextSeed() 方法先 UNSAFE.getLong
 *
 */
public class ThreadLocalRandomDemo {
    public static void main(String[] args) {
        // 获取的是一个单例
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < 10; i++) {
            System.out.println(random.nextInt(5));
        }
    }
}
