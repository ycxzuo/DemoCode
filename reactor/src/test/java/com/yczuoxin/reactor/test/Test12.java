package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class Test12 {

    @Test
    public void testDelayElements() {
        Flux.range(0, 10)
                .delayElements(Duration.ofMillis(10))
                .log()
                .blockLast();
    }
    @Test
    public void testParallelFlux() throws InterruptedException {
        Flux.range(1, 10)
                .publishOn(Schedulers.elastic())
                .parallel(2)
                .runOn(Schedulers.parallel())
                .log().subscribe();
        TimeUnit.MILLISECONDS.sleep(10);
    }

}
