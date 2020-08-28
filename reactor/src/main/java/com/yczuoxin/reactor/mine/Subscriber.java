package com.yczuoxin.reactor.mine;

public interface Subscriber<T> {

    void onSubscriber(Subscription subscription);
    void onNext(T t);
    void onError(Throwable t);
    void onComplete();

}
