package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SignalType;

import java.util.concurrent.atomic.LongAdder;

public class Test5 {

    @Test
    public void finallyTest(){
        LongAdder statsCancel = new LongAdder();    // 1
        Flux.just("foo", "bar")
                .doFinally(type -> {
                    // SignalType 是 Reactor 的状态
                    if (type == SignalType.CANCEL) {
                        statsCancel.increment();
                        System.out.println("finally");
                    }
                })
                .take(1)
                .subscribe(System.out::println);
        System.out.println(statsCancel.intValue());
    }

}
