package com.yczuoxin.reactor.mine;

public class MyReactorTest {
    public static void main(String[] args) {
        Flux.just(1,2,3,4,5,6).subscribe(new Subscriber<Integer>() {
            @Override
            public void onSubscriber(Subscription subscription) {
                System.out.println("onSubscribe");
                subscription.request(6);
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext:" + integer);
            }

            @Override
            public void onError(Throwable t) {

                System.out.println(t.getMessage());
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }
}
