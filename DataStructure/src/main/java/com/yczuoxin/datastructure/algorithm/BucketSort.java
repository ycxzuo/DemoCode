package com.yczuoxin.datastructure.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 桶排序
 */
public class BucketSort {

    public static void main(String[] args) {
        sort(Arrays.asList(5,2,4,6,8,1,7,3,9,2)).forEach(System.out::println);
    }

    public static List<Integer> sort(List<Integer> list) {
        if (list.isEmpty()) {
            return list;
        }
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        int size = list.size();
        List<Integer> result = new ArrayList<>(size);
        // 找到最大值和最小值
        for (int i = 0; i < size; i++) {
            max = Math.max(max, list.get(i));
            min = Math.min(min, list.get(i));
        }
        // 创建桶
        int bucketNumber = (max - min) / size + 1;
        List<List<Integer>> bucketList = new ArrayList<>(bucketNumber);
        for (int i = 0; i < bucketNumber; i++) {
            bucketList.add(new ArrayList<>());
        }
        // 放入桶
        for (int i = 0; i < size; i++) {
            bucketList.get((list.get(i) - min) / size).add(list.get(i));
        }
        // 存入结果集
        for (int i = 0; i < bucketNumber; i++) {
            // 可以使用任意的排序方式
            Collections.sort(bucketList.get(i));
            result.addAll(bucketList.get(i));
        }
        return result;
    }
}
