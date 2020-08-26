package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Test8 {

    @Test
    public void testGenerate() {
        final AtomicInteger count = new AtomicInteger(1);
        Flux.generate(sink -> {
            sink.next(count.get() + " : " + new Date());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (count.getAndIncrement() >= 5) {
                sink.complete();
            }
        }).subscribe(System.out::println);
    }

    @Test
    public void testGenerate2() {
        Flux.generate(
                () -> 1,
                (count, sink) -> {
                    sink.next(count + " : " + new Date());
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (count >= 5) {
                        sink.complete();
                    }
                    return count + 1;
                }, System.out::println) // sink 内的数据执行完后执行，可以用做关闭流之类的操作
                .subscribe(System.out::println);
    }

}
