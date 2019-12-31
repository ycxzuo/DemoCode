package com.yczuoxin.datastructure.structure;

public class SingleLinkedList<E> {

    Node<E> head = null;
    Node<E> tail = null;
    int size = 0;

    private class Node<E> {
        private E e;
        private Node<E> next;

        public Node(E e) {
            this.e = e;
        }

        public E getE() {
            return e;
        }
    }

    public synchronized void addFirst(E e) {
        if (head == null) {
            tail = head = new Node<E>(e);
            size++;
            return;
        }
        Node<E> newHead = new Node<>(e);
        newHead.next = head;
        head = newHead;
        size++;
    }

    public void addTail(E e) {
        if (null == e) {
            throw new NullPointerException("数据不能为空");
        }
        synchronized (this) {
            if (head == null) {
                addFirst(e);
                size++;
                return;
            }
            Node<E> newTail = new Node<>(e);
            tail.next = newTail;
            tail = newTail;
            size++;
        }
    }

    public synchronized boolean remove(Object o) {
        if (head == null) {
            return false;
        }
        Node<E> current = head;
        Node<E> prev = null;
        while (current != null) {
            if (current.e == o) {
                if (current == head) {
                    head = current.next;
                } else {
                    prev.next = current.next;
                }
                // help GC
                current.e = null;
                current.next = null;
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false;
    }

    public synchronized E find(int index) {
        if (index >= size || index < 0) {
            throw new ArrayIndexOutOfBoundsException("超过了链表长度" + size);
        }
        E e;
        Node<E> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.e;
    }

    public synchronized int indexOf(E e) {
        if (null == e) {
            return -1;
        }
        int index = 0;
        Node<E> current = head;
        while (current != null) {
            if (current.e == e) {
                return index;
            }
            index++;
            current = current.next;
        }
        return -1;
    }
}
