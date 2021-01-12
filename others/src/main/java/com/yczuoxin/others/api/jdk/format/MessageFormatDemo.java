package com.yczuoxin.others.api.jdk.format;

import java.text.MessageFormat;

public class MessageFormatDemo {
    public static void main(String[] args) {
        String pattern = "aaaaa:{0,number, #}";
        String format = MessageFormat.format(pattern, 100000);
        System.out.println(format);
    }
}
