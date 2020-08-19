package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Test6 {
    @Test
    public void retryTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        Flux.range(1, 6)
                .map(i -> 10 / (3 - i))
                .retry(1)
                .subscribe(System.out::println, System.err::println, latch::countDown);
        latch.await(2, TimeUnit.SECONDS);
    }
}
