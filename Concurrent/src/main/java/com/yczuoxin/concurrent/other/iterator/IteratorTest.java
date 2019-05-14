package com.yczuoxin.concurrent.other.iterator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class IteratorTest {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(0,2,4,5,6,7,8);
        List list1 = new ArrayList(list);
        Iterator<Integer> iterator = list1.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            iterator.remove();
            System.out.println(list1.size());
        }
    }
}
