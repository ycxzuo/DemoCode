package com.yczuoxin.concurrent.demo;

public class Demo3 {
    public static void main(String[] args) {
        Language language = new Language();
        System.out.println(language.language == EnumLanguage.JAVA);
    }
}
