package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 快速排序
 */
public class QuickSort {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        sort(Arrays.asList(5,2,4,6,8,1,7,3,9,2)).forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        exchange(0, list.size() - 1, list);
        return list;
    }

    private static void exchange (int start, int end, List<Integer> list) {
        // 左游标
        int left = start;
        // 右游标
        int right = end;
        // 基准值，目的就是给基准值找到自己的在 list 的位置
        int base = list.get(left);
        while (right > left) {
            // 先从右边开始遍历找到第一个小于基准值的数字
            while (base <= list.get(right) && right > left){
                right--;
            }
            if (list.get(right) <= base) {
                // 将右边第一个小于基准值放在基准值之前的位置上
                list.set(left, list.get(right));
            } else {
                // 该值是最小值，不用排
                exchange(left + 1, end, list);
                break;
            }
            // 再从左边游标的开始找大于基准值的数字
            while (list.get(left) <= base && right > left) {
                left++;
            }
            if (list.get(left) >= base) {
                // 将左边第一个小于基准值放在之前的小于基准值的位置上
                list.set(right, list.get(left));
            }
        }
        // 基准值落位
        if (left == right) {
            list.set(left, base);
        }
        // 如果起始 index 小于左游标
        if (start < left) {
            // 继续排基准值的左边
            exchange(start, left -1, list);
        }
        // 如果末尾 index 大于右游标
        if (end > right) {
            // 继续排基准值的右边
            exchange(right + 1, end, list);
        }
    }
}
