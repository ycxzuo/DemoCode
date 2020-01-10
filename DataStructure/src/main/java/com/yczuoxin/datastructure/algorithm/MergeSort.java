package com.yczuoxin.datastructure.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSort {
    public static void main(String[] args) {
        sort(Arrays.asList(2, 5, 3, 4, 1, 8)).forEach(System.out::println);
    }

    private static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        merge(list, 0, list.size() - 1);
        return list;
    }

    private static void merge(List<Integer> list, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        // 将数据分割为有序的左 list 和有序的右 list，并分别进行排序
        merge(list, left, mid);
        merge(list, mid + 1, right);
        // 用一个临时 list 存储原始 list
        List<Integer> tempList = new ArrayList(list);
        // 左 list 游标
        int leftIndex = left;
        // 右 list 游标
        int rightIndex = mid + 1;
        // 当前存入 list 的 index
        int currentIndex = left;
        // 比较左 list 和右 list 的游标上的值，谁小就排在前面
        while (leftIndex <= mid && rightIndex <= right) {
            if (tempList.get(leftIndex) >= tempList.get(rightIndex)) {
                list.set(currentIndex++, tempList.get(rightIndex++));
            } else {
                list.set(currentIndex++, tempList.get(leftIndex++));
            }
        }
        // 如果左边 list 先排完，将右边 list 的值按顺序插入到 list 的尾部
        while (rightIndex <= right) {
            list.set(currentIndex++, tempList.get(rightIndex++));
        }
        // 如果右边 list 先排完，将左边 list 的值按顺序插入到 list 的尾部
        while (leftIndex <= mid) {
            list.set(currentIndex++, tempList.get(leftIndex++));
        }

    }
}
