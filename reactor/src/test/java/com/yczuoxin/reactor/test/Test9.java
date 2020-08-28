package com.yczuoxin.reactor.test;

import com.yczuoxin.reactor.event.MyEventListener;
import com.yczuoxin.reactor.event.MyEventSource;
import org.junit.Test;
import reactor.core.publisher.Flux;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Test9 {

    @Test
    public void testCreate() throws InterruptedException {
        MyEventSource eventSource = new MyEventSource();
        Flux.create(sink-> {
            eventSource.register(new MyEventListener() {
                @Override
                public void onPushEvent(MyEventSource.MyEvent event) {
                    sink.next(event);
                }

                @Override
                public void onEventStopped() {
                    sink.complete();
                }
            });
            sink.onRequest(n -> {
                System.out.println(n);
            });
        }).subscribe(System.out::println);

        for (int i = 0; i < 30; i++) {
            Random random = new SecureRandom();
            TimeUnit.SECONDS.sleep(1);
            eventSource.pushEvent(new MyEventSource.MyEvent(new Date(), "event - " + i));
        }
    }

}
