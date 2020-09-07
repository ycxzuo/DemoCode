package com.yczuoxin.reactor.test;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.function.Function;

public class Test13 {
    @Test
    public void testTransform() {
        // transform 可以将一段操作链打包为一个函数式
        // 这个函数式能在组装期将被封装的操作符还原并接入到调用 transform 的位置
        // 这样做和直接将被封装的操作符加入到链上的效果是一样的
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap)
                .subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: "+d));
    }
}
