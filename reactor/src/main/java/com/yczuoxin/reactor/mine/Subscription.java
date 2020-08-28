package com.yczuoxin.reactor.mine;

public interface Subscription {

    void request(long n);

    void cancel();

}
