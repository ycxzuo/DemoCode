package com.yczuoxin.reactor.mine;

public class ArrayFlux<T> extends Flux<T> {

    T[] array;

    public ArrayFlux(T[] array) {
        this.array = array;
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        subscriber.onSubscriber(new ArraySubscription<>(array, subscriber));
    }

    static class ArraySubscription<T> implements Subscription {

        final Subscriber<? super T> subscriber;
        T[] array;
        int index;
        volatile boolean cancel;

        public ArraySubscription(T[] array, Subscriber<? super T> subscriber) {
            this.array = array;
            this.subscriber = subscriber;
        }

        @Override
        public void request(long n) {
            if (cancel) {
                return;
            }
            long length = array.length;
            for (int i = 0; i < n && index < length; i++) {
                try {
                    subscriber.onNext(array[index++]);
                } catch (Exception e) {
                    subscriber.onError(e);
                    this.cancel = true;
                    return;
                }
            }
            if (index == length) {
                subscriber.onComplete();
            }
        }

        @Override
        public void cancel() {
            this.cancel = true;
        }
    }
}
