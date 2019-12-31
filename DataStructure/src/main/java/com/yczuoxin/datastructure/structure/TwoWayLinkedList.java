package com.yczuoxin.datastructure.structure;

public class TwoWayLinkedList<E> {

    private int size= 0;
    private Node<E> first;
    private Node<E> tail;

    private class Node<E> {
        private E e;
        private Node<E> prev;
        private Node<E> next;

        public Node(E e) {
            this.e = e;
        }

        public E getE() {
            return e;
        }
    }

    public void addFirst(E e) {
        if (null == e) {
            throw new NullPointerException("数据不能为空");
        }
        Node<E> newNode = new Node<>(e);
        synchronized (this) {
            if (null == first) {
                first = tail = newNode;
            } else {
                newNode.next = first;
                first.prev = newNode;
                first = newNode;
            }
            size++;
        }
    }

    public void addTail(E e) {
        if (null == e) {
            throw new NullPointerException("数据不能为空");
        }
        Node<E> newNode = new Node<>(e);
        synchronized (this) {
            if (null == first) {
                first = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }
            size++;
        }
    }

    public synchronized E indexOf (int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("超过了链表长度" + size);
        }
        if (index < (size >> 1)) {
            Node<E> current = first;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current.e;
        } else {
            Node<E> current = tail;
            for (int i = 1; i < size - index; i++) {
                current = current.prev;
            }
            return current.e;
        }
    }

    public synchronized int find (E e) {
        if (null == e || null == first) {
            return -1;
        }
        Node<E> current = first;
        int index = 0;
        while (current != null) {
            if (e == current.e) {
                return index;
            }
            index++;
            current = current.next;
        }
        return -1;
    }

    public synchronized boolean remove(Object o) {
        if (null == o || null == first) {
            return false;
        }
        Node<E> current = first;
        while (current != null) {
            if (o == current.e) {
                current.prev.next = current.next;
                current.next.prev = current.prev;
                size--;
                // help GC
                current.e = null;
                current.prev = null;
                current.next = null;
                return true;
            }
            current = current.next;
        }
        return false;
    }

    public synchronized int size(){
        return size;
    }
}
