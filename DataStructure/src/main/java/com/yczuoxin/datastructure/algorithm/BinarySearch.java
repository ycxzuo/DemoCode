package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * 二分查找，要求列表是有序的
 */
public class BinarySearch {
    public static void main(String[] args) {
        int index = calculate(Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15), 11);
        System.out.println(index);
    }

    public static int calculate (List<Integer> list, int target) {
        int low = 0;
        int high = list.size();
        int mid;
        // 从中间找，如果小于就找左边的一部分，递归查找
        while (low <= high) {
            mid = (high + low) / 2;
            if (list.get(mid) == target) {
                return mid;
            } else if (target > list.get(mid)) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return -1;
    }
}
