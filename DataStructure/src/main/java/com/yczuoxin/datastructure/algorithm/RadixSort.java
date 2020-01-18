package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 基数排序
 */
public class RadixSort {

    public static void main(String[] args) {
        sort(Arrays.asList(310, 10, 3, 16, 18, 103, 7, 32, 27, 6)).forEach(System.out::println);
    }

    public static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        // 找到最高位的位数
        int size = list.size();
        int maxLength = 0;
        for (int i = 0; i < size; i++) {
            int tempLength = String.valueOf(list.get(i)).length();
            if (tempLength > maxLength) {
                maxLength = tempLength;
            }
        }

        Integer[] data = list.toArray(new Integer[size]);
        radix(data, 0, maxLength);
        return Arrays.asList(data);
    }

    private static void radix(Integer[] array, int digit, int maxLength) {
        if (maxLength > digit) {
            // 装载数据的容器
            Integer[][] bucket = new Integer[10][array.length];
            // 数组 bucket 中对应数值数组数量 - 1，也就是下一个插入数值放入的二维数组的下标
            int[] order = new int[10];
            for (Integer integer : array) {
                // 找到当前位上的数值
                int index = (integer / (int) Math.pow(10, digit)) % 10;
                // 存入容器对应的第一维度的数组中
                bucket[index][order[index]] = integer;
                // 第一维度数组的数量加一，所以下一个数值要往后加一
                order[index]++;
            }
            // 整理数据为一维数组
            int number = 0;
            for (int i = 0; i < bucket.length; i++) {
                if (order[i] != 0) {
                    for (int j = 0; j < order[i]; j++) {
                        array[number++] = bucket[i][j];
                    }
                }
            }
            radix(array, ++digit, maxLength);
        }
    }
}
