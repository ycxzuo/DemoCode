package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 优化版的插入排序
 * 插入排序在数据有一定顺序的序列的场景下排序效率较高
 * 希尔排序实现将数据先排序了一波
 */
public class ShellSort {
    public static void main(String[] args) {
        sort(Arrays.asList(2,5,3,4,1,8,7)).forEach(System.out::println);
    }

    public static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        int increment = list.size() / 2;
        while (increment > 0) {
            sortByIncrement(list, increment);
            increment = increment / 2;
        }
        return list;
    }

    public static void sortByIncrement (List<Integer> list, int increment) {
        for (int i = 0; i < increment; i++) {
            // 拿到第二个元素，开始做插入排序
            int index = i + increment;
            while (index < list.size()) {
                // 从增量前面一个元素开始向前遍历
                int current = index - increment;
                // 需要插入的元素
                int data = list.get(index);
                // 带步长的插入排序
                while (current >= 0 && list.get(current) > data) {
                    list.set(current + increment, list.get(current));
                    current -= increment;
                }
                list.set(current + increment, data);
                index = index + increment;
            }
        }
    }
}
