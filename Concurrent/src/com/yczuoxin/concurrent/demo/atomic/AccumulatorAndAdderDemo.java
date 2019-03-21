package com.yczuoxin.concurrent.demo.atomic;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * 这两种写法效果是一样的
 */
public class AccumulatorAndAdderDemo {
    public static void main(String[] args) {
        LongAdder adder = new LongAdder();

        LongAccumulator accumulator = new LongAccumulator(((left, right) -> left + right), 0);
    }
}
