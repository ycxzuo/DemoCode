package com.yczuoxin.concurrent.demo.atomic;

import java.util.concurrent.atomic.LongAdder;

public class LongAdderDemo {
    public static void main(String[] args) throws InterruptedException {
        LongAdder adder = new LongAdder();
        Thread threadOne = new Thread(() -> adder.add(1L));
        Thread threadTwo = new Thread(() -> adder.add(2L));
        Thread threadThree = new Thread(() -> adder.add(3L));
        Thread threadFour = new Thread(() -> adder.add(4L));
        Thread threadFive = new Thread(() -> adder.add(5L));

        threadOne.start();
        threadTwo.start();
        threadThree.start();
        threadFour.start();
        threadFive.start();

        threadOne.join();
        threadTwo.join();
        threadThree.join();
        threadFour.join();
        threadFive.join();
        System.out.println(adder.longValue());
    }
}
