package com.yczuoxin.others.reference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        Map<String, List<String>> map = new HashMap<>();
        map.put("a", list);
        list.add("b");
        System.out.println(map.get("a"));
    }

}
