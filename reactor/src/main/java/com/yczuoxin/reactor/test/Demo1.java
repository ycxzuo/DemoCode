package com.yczuoxin.reactor.test;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Demo1 {
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5,6);
        Mono.just(1);
        Integer[] array = new Integer[]{1,2,3,4,5,6};
        Flux.fromArray(array);
        List<Integer> list = Arrays.asList(array);
        Flux.fromIterable(list);
        Stream<Integer> stream = list.stream();
        Flux.fromStream(stream);
    }
}
