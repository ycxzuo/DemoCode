package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

public class BubbleSort {
    public static void main(String[] args) {
        sort(Arrays.asList(2,5,3,4,1,8)).forEach(System.out::println);
    }

    public static List<Integer> sort (List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j) > list.get(j + 1)) {
                    Integer temp = list.get(j) ^ list.get(j + 1);
                    list.set(j, temp ^ list.get(j));
                    list.set(j + 1, temp ^ list.get(j));
                }
            }
        }
        return list;
    }
}
