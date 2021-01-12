package com.yczuoxin.others.api.jdk.format;

import java.text.NumberFormat;
import java.util.Locale;

public class NumberFormatDemo {

    public static void main(String[] args) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.CHINA);
        // 去掉千分位的逗号分组
        numberFormat.setGroupingUsed(false);
        String format = numberFormat.format(123456);
        System.out.println(format);

        NumberFormat currencyNumberFormat = NumberFormat.getCurrencyInstance(Locale.CHINA);
        String currentNumber = currencyNumberFormat.format(132456);
        System.out.println(currentNumber);
    }

}
