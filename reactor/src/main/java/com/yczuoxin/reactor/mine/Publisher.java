package com.yczuoxin.reactor.mine;

public interface Publisher<T> {
    void subscribe(Subscriber<? super T> subscriber);
}
