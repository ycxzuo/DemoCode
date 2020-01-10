package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 插入排序
 */
public class InsertSort {
    public static void main(String[] args) {
        sort(Arrays.asList(2,5,3,4,1,8)).forEach(System.out::println);
    }

    public static List<Integer> sort (List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        for (int i = 1; i < list.size(); i++) {
            int index = i - 1;
            int currentValue = list.get(i);
            while (currentValue < list.get(index) && index >= 0) {
                list.set(index + 1, list.get(index));
                index--;
            }
            list.set(index + 1, currentValue);
        }
        return list;
    }
}
