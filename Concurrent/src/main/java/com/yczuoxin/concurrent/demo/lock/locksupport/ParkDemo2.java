package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * park(Object blocker) 方法
 *
 * Thread 中有一个 volatile Object parkBlocker 字段，用来存放 park() 方法传入的 blocker 对象，
 * 也就是把 blocker 变量存放到了调用 park 方法的线程的成员变量中
 *
 * 在线程被激活后，会清除 blocker 信息
 */
public class ParkDemo2 {

    public void testPark() {
        LockSupport.park(this);
    }

    public static void main(String[] args) {
        ParkDemo2 demo = new ParkDemo2();
        demo.testPark();
    }
}
