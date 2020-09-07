package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class Test14 {
    @Test
    public void testTransformDeferred() {
        AtomicInteger atomicInteger = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (atomicInteger.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        // 有状态的，会再次取到 filterAndMap 而不是直接拿到之前的 filterAndMap 使用
                        .transformDeferred(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }

}
