package com.yczuoxin.reactor.test;

import com.yczuoxin.reactor.event.MyEventListener;
import com.yczuoxin.reactor.event.MyEventSource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class test10 {
    private final int EVENT_DURATION   = 10;    // 生成的事件间隔时间，单位毫秒
    private final int EVENT_COUNT      = 20;    // 生成的事件个数
    private final int PROCESS_DURATION = 30;    // 订阅者处理每个元素的时间，单位毫秒

    private Flux<MyEventSource.MyEvent> fastPublisher;
    private SlowSubscriber slowSubscriber;
    private MyEventSource eventSource;
    private CountDownLatch countDownLatch;
    private FluxSink.OverflowStrategy strategy = FluxSink.OverflowStrategy.BUFFER;


    private Flux<MyEventSource.MyEvent> createFlux(FluxSink.OverflowStrategy strategy) {
        return Flux.create(sink -> eventSource.register(new MyEventListener() {
            @Override
            public void onPushEvent(MyEventSource.MyEvent event) {
                System.out.println("publish >>> " + event.getMessage());
                sink.next(event);
            }

            @Override
            public void onEventStopped() {
                sink.complete();
            }
        }), strategy);
    }

    private void generateEvent(int times, int millis) {
        for (int i = 0; i < times; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(millis);
            } catch (InterruptedException e) {

            }
            eventSource.pushEvent(new MyEventSource.MyEvent(new Date(), "Event - " + i));
        }
        eventSource.stopped();
    }

    @Before
    public void setup() {
        countDownLatch = new CountDownLatch(1);
        slowSubscriber = new SlowSubscriber();
        eventSource = new MyEventSource();
    }

    @After
    public void subscribe() throws InterruptedException {
        fastPublisher.subscribe(slowSubscriber);
        generateEvent(EVENT_COUNT, EVENT_DURATION);
        countDownLatch.await(1, TimeUnit.MINUTES);
    }

    @Test
    public void testStrategy() {
        fastPublisher =
                createFlux(strategy)
                        .doOnRequest(n -> System.out.println("         ===  request: " + n + " ==="))
                        .publishOn(Schedulers.newSingle("newSingle"), 2);
    }

    // 会以最后一个 onBackpressureXxx 为准
    @Test
    public void testOnBackPressure() {
        fastPublisher = createFlux(strategy)
                .onBackpressureBuffer()
                .onBackpressureDrop(System.out::println)
                //.onBackpressureLatest()
                //.onBackpressureError()
                .doOnRequest(n -> System.out.println("         ===  request: " + n + " ==="))
                .publishOn(Schedulers.newSingle("newSingle"), 2);
    }

    private class SlowSubscriber extends BaseSubscriber<MyEventSource.MyEvent> {
        @Override
        protected void hookOnSubscribe(Subscription subscription) {
            request(1);
        }

        @Override
        protected void hookOnNext(MyEventSource.MyEvent event) {
            System.out.println("                   receive <<< " + event.getMessage());
            try {
                TimeUnit.MILLISECONDS.sleep(PROCESS_DURATION);
            } catch (InterruptedException e) {

            }
            request(1);
        }

        @Override
        protected void hookOnError(Throwable throwable) {
            System.out.println("                     receive <<< " + throwable.getMessage());
        }

        @Override
        protected void hookOnComplete() {
            countDownLatch.countDown();
        }
    }
}
