package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

public class Test4 {

    @Test
    public void errorTest() {
        Flux.range(1, 6)
                .map(i -> 10/(i - 3))
                .doOnError(e -> System.out.println(e.getMessage()))
                .onErrorContinue((e, i)-> {
                    System.out.println("e: " + e.getMessage());
                    System.out.println("i: " + i);
                })
                .map(i -> i * i)
                .subscribe(System.out::println, System.err::println);
    }

}
