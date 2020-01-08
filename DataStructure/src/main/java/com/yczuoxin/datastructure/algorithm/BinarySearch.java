package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

public class BinarySearch {
    public static void main(String[] args) {

        int index = calculate(Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15), 11);
        System.out.println(index);
    }

    public static int calculate (List<Integer> list, int target) {
        int low = 0;
        int high = list.size();
        int mid;
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
