package com.yczuoxin.reactor.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Demo3 {
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5,6).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Completed!")
        );
        System.out.println("-------------------------------");
        Mono.error(new Exception("error message")).subscribe(
                System.out::println,
                System.err::println,
                () -> System.out.println("Completed!")
        );
    }
}
