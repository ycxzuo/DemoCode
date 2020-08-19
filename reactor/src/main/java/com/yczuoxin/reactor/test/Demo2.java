package com.yczuoxin.reactor.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class Demo2 {
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5,6).subscribe(System.out::print);
        System.out.println();
        Mono.just(1).subscribe(System.out::print);
    }
}
