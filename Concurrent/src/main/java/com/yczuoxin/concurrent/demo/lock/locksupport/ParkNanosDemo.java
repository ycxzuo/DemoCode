package com.yczuoxin.concurrent.demo.lock.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 与 park() 方法类似，如果调用 park() 方法的线程已经拿到了与 LockSupport 关联的许可证，
 * 则调用 LockSupport.parkNanos(long nanos) 方法后会马上返回，如果没有拿到许可证，则调
 * 用线程会被挂起 nanos 时间后修改为自动返回
 *
 * nanos 纳秒
 */
public class ParkNanosDemo {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park!");
            LockSupport.parkNanos(2000000000L);
            System.out.println("child thread unpark!");
        });

        thread.start();

        // TimeUnit.SECONDS.sleep(1);

        thread.join();
        // 超时后会自动返回
        // System.out.println("main thread begin unpark!");

        // LockSupport.unpark(thread);
    }
}
