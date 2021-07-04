package com.yczuoxin.others.api.jdk.map;

import java.util.LinkedHashMap;

public class LinkedHashMapTest {

    public static void main(String[] args) {
        LinkedHashMap<Integer, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(3, "3");
        linkedHashMap.put(2, "2");
        linkedHashMap.put(1, "1");
        linkedHashMap.put(4, "4");
        linkedHashMap.put(5, "5");
        linkedHashMap.put(6, "6");
        linkedHashMap.put(7, "7");
        linkedHashMap.put(1, "1");

        linkedHashMap.forEach((key, value) -> {
            System.out.println("key: " + key);
            System.out.println("value: " + value);
            System.out.println("-----------------------------");
        });
    }

}
