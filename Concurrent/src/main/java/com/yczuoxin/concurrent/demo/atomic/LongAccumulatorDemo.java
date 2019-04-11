package com.yczuoxin.concurrent.demo.atomic;

import java.util.concurrent.atomic.LongAccumulator;

public class LongAccumulatorDemo {
    public static void main(String[] args) throws InterruptedException {
        LongAccumulator accumulator = new LongAccumulator((left, right) -> left * right, 10);
        Thread threadOne = new Thread(() -> accumulator.accumulate(1));
        Thread threadTwo = new Thread(() -> accumulator.accumulate(2));
        Thread threadThree = new Thread(() -> accumulator.accumulate(3));
        Thread threadFour = new Thread(() -> accumulator.accumulate(4));
        Thread threadFive = new Thread(() -> accumulator.accumulate(5));

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

        System.out.println(accumulator.intValue());
    }
}
