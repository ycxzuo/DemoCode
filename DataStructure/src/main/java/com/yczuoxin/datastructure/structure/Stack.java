package com.yczuoxin.datastructure.structure;

/**
 * 定长的堆栈
 */
public class Stack<E> {
    int threshold;
    int top;
    protected Object[] elements;

    public Stack() {
        init(10);
    }

    public Stack(int threshold) {
        init(threshold);
    }

    private void init(int threshold) {
        if (threshold <= 0) {
            throw new IllegalArgumentException("栈空间不能小于或等于 0");
        }
        this.threshold = threshold;
        this.top = -1;
        this.elements = new Object[threshold];
    }

    public void push(E e) {
        if (e == null) {
            throw new NullPointerException("数据不能为空");
        }
        synchronized (this) {
            if (top == threshold - 1) {
                throw new RuntimeException("栈已经满了:" + threshold);
            }
            elements[++top] = e;
        }
    }

    public synchronized E pop() {
        E e = peek();
        elements[top] = null;
        top--;
        return e;
    }

    public synchronized E peek() {
        if (top == -1) {
            throw new RuntimeException("栈已经空了");
        }
        return (E) elements[top];
    }

    public int size() {
        return top + 1;
    }
}
