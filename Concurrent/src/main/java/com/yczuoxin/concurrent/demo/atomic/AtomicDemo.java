package com.yczuoxin.concurrent.demo.atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 原子性操作计算数组中 0 的个数
 */
public class AtomicDemo {
    // 创建 Long 型原子计数器
    private static AtomicLong atomicLong = new AtomicLong();
    //
    private static Integer[] arrayOne = new Integer[]{0,1,2,3,0,5,6,0,56,0};
    private static Integer[] arrayTwo = new Integer[]{10,1,2,3,0,5,6,0,56,0};

    public static void main(String[] args) throws InterruptedException {
        // 计算数组 arrayOne 中 0 的个数
        Thread threadOne = new Thread(() -> {
            int size = arrayOne.length;
            for (int i = 0; i < size; i++) {
                if (arrayOne[i].intValue() == 0) {
                    atomicLong.incrementAndGet();
                }
            }
        });
        // 计算数组 arrayTwo 中 0 的个数
        Thread threadTwo = new Thread(() -> {
            int size = arrayTwo.length;
            for (int i = 0; i < size; i++) {
                if (arrayTwo[i].intValue() == 0) {
                    atomicLong.incrementAndGet();
                }
            }
        });

        threadOne.start();
        threadTwo.start();

        threadOne.join();
        threadTwo.join();

        System.out.println("count 0: " + atomicLong.get());
    }
}
