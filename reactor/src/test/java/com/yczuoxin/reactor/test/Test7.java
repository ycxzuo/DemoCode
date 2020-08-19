package com.yczuoxin.reactor.test;

import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;

public class Test7 {

    @Test
    public void testBackpressure() {
        Flux.range(1, 6)
                .log()
                .doOnRequest(n -> System.out.println("Request " + n + " values..."))
                .subscribe(new BaseSubscriber<Integer>() {
                    private int count = 0;
                    private final int requestCount = 2;

                    // 订阅的时候操作
                    @Override
                    protected void hookOnSubscribe(Subscription subscription) {
                        request(requestCount);
                    }

                    // 收到数据的时候操作
                    @Override
                    protected void hookOnNext(Integer value) {
                        count++;
                        if (count == requestCount) {
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("need data from publisher");
                            request(requestCount);
                            count = 0;
                        }
                    }
                });
    }

    @Test
    public void testBackpressure2() {
        Flux.range(1, 6).limitRate(2).subscribe(System.out::println);
    }

}
