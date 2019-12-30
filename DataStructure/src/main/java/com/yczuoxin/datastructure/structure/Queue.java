package com.yczuoxin.datastructure.structure;

public class Queue<E> {
    int threshold;
    int currentSize;
    int first;
    int tail;
    Object[] elements;

    public Queue(int threshold) {
        init(threshold);
    }

    public Queue() {
        init(10);
    }

    private void init(int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("队列空间不能小于或等于 0");
        }
        this.threshold = threshold;
        this.currentSize = this.first = this.tail = 0;
        elements = new Object[threshold];
    }

    public void offer(E e) {
        if (null == e) {
            throw new NullPointerException("数据不能为空");
        }
        synchronized (this) {
            if (threshold == currentSize) {
                throw new RuntimeException("队列已经满了:" + threshold);
            }
            if (tail == threshold) {
                elements[0] = e;
                tail = 1;
            } else {
                elements[tail++] = e;
            }
            currentSize++;
        }

    }

    public synchronized E poll() {
        E e = peek();
        elements[first++] = null;
        currentSize--;
        return e;
    }


    public synchronized E peek() {
        if (currentSize == 0) {
            throw new RuntimeException("队列已经空了");
        }
        if (first == threshold) {
            first = 0;
        }
        return (E) elements[first];
    }

    public int size() {
        return currentSize;
    }
}
