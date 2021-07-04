package com.yczuoxin.others.api.jdk.concurrent.order;

import java.util.concurrent.CountDownLatch;

public class OrderDemo {

    private static int a = 0, b = 0, x = 0, y = 0;

    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            a = 0;
            b = 0;
            x = 0;
            y = 0;
            CountDownLatch latch = new CountDownLatch(2);
            Thread t1 = new Thread(() -> {
                a = 1;
                x = b;
                latch.countDown();
            });
            Thread t2 = new Thread(() -> {
                b = 1;
                x = a;
                latch.countDown();
            });

            t1.start();
            t2.start();
            latch.await();
            if (x == 0 && y == 0) {
                System.out.println("经历了" + i + "次 x = y");
            }
        }
    }
}
