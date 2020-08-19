package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test2 {
    private Flux<String> getZipDescFlux() {
        String desc = "Zip two sources together, that is to say wait for all the sources to emit one element and combine these elements once into a Tuple2.";
        return Flux.fromArray(desc.split("\\s+"));
    }

    @Test
    public void testSimpleOperators() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1); // 让线程等待，否则会直接返回
        Flux.zip(getZipDescFlux(), Flux.interval(Duration.ofMillis(200)))  // Flux 流每 200ms 发射一条数据
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testSimpleOperators2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1); // 让线程等待，否则会直接返回
        getZipDescFlux().zipWith(Flux.interval(Duration.ofMillis(200)))
                .subscribe(t -> System.out.println(t.getT1()), null, countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);
    }
}
