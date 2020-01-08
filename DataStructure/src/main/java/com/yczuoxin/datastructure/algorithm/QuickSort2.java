package com.yczuoxin.datastructure.algorithm;

import java.util.Arrays;
import java.util.List;

// 栈深度高，速度会慢一丢丢
public class QuickSort2 {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        sort(Arrays.asList(5,2,4,6,8,1,7,3,9)).forEach(System.out::println);
        System.out.println(System.currentTimeMillis() - start);
    }

    private static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        int start = 0;
        int end = list.size() - 1;
        exchange(start, end, list);
        return list;
    }

    private static void exchange (int start, int end, List<Integer> list) {
        if (start < end) {
            int left = start;
            int right = end;
            int base = list.get(left);
            while (right > left) {
                while (base < list.get(right) && right > left){
                    right--;
                }
                if (list.get(right) < base) {
                    list.set(left, list.get(right));
                }
                while (list.get(left) < base && right > left) {
                    left++;
                }
                if (list.get(left) > base) {
                    list.set(right, list.get(left));
                }
            }
            if (left == right) {
                list.set(left, base);
            }
            exchange(start, left -1, list);
            exchange(right + 1, end, list);
        }

    }
}
