package com.yczuoxin.datastructure.demo;

import com.yczuoxin.datastructure.structure.TwoWayLinkedList;

public class TwoWayLinkedListDemo {
    public static void main(String[] args) {
        TwoWayLinkedList<String> linkedList = new TwoWayLinkedList<>();
        linkedList.addTail("2");
        linkedList.addTail("3");
        linkedList.addTail("4");
        linkedList.addTail("5");
        linkedList.addFirst("1");

        System.out.println(linkedList.indexOf(2));
        System.out.println(linkedList.find("4"));
        linkedList.remove("2");
        System.out.println(linkedList.size());
    }
}
