package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 冒泡排序
 */
public class BubbleSort {
    public static void main(String[] args) {
        sort(Arrays.asList(2,5,3,4,1,8,6)).forEach(System.out::println);
    }

    public static List<Integer> sort (List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        // 从第一个数开始排，直到倒数第二个数时停下，因为前面的数字都找到了自己的位置
        // 最后一个数位置也已经定好了
        for (int i = 0; i < list.size() - 1; i++) {
            // 依次与后面的数比较，如果大，就往后排，直到倒数第二个数时停下，原因同上
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
