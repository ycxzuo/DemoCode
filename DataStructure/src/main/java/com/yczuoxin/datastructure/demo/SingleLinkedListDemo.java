package com.yczuoxin.datastructure.demo;

import com.yczuoxin.datastructure.structure.SingleLinkedList;

public class SingleLinkedListDemo {
    public static void main(String[] args) {
        SingleLinkedList<String> linkedList = new SingleLinkedList<>();
        linkedList.addTail("a");
        linkedList.addTail("b");
        linkedList.addTail("c");
        linkedList.addFirst("d");

        System.out.println(linkedList.find(3));
        System.out.println(linkedList.indexOf("b"));
    }
}
