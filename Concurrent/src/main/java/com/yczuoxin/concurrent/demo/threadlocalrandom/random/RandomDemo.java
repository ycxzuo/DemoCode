package com.yczuoxin.concurrent.demo.threadlocalrandom.random;

import java.util.Random;

/**
 * 生成随机数的方法
 * 由于随机数算法的函数是相同的，其利用老的种子去计算新的种子
 * 单线程可以保证随机性，但是多线程调用，可能会拿到相同的老的种子，
 * 导致多个线程拿到相同的随机值
 *
 * 为了解决这个问题，在 Random 的 next() 方法中，使用了 CAS 操作保证了其原子性，
 *
 * 但是同时又产生了另一个问题，如果同时多个线程计算随机数，会导致许多线程进入自旋重试，
 * 这样会降低性能
 */
public class RandomDemo {
    public static void main(String[] args) {
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            // 生成 [0, 5) 的整数随机数
            System.out.println(random.nextInt(5));
        }
    }
}
