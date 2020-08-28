package com.yczuoxin.reactor.mine;

public abstract class Flux<T> implements Publisher<T> {

    public static <T> Flux<T> just(T... data) {
        return new ArrayFlux<>(data);
    }

}
