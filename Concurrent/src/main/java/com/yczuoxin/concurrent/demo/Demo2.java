package com.yczuoxin.concurrent.demo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Demo2 {
    public static void main(String[] args) {
        List<String> sortedList = Arrays.asList("唐", "宋", "元", "明", "清");

        List<String> unsortedList = Arrays.asList("元", "清", "宋", "明", "唐");

        System.out.println(unsortedList);//输出：[元, 清, 宋, 明, 唐]

        unsortedList.sort(Comparator.comparingInt(sortedList::indexOf));

        System.out.println(unsortedList);//输出：[唐, 宋, 元, 明, 清]
    }
}
