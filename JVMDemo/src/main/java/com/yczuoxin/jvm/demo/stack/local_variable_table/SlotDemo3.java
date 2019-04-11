package com.yczuoxin.jvm.demo.stack.local_variable_table;

/**
 *  VM options: -verbose:gc
 */
public class SlotDemo3 {
    public static void main(String[] args) {
        {
            byte[] placeholder = new byte[64 * 1024 * 1024];
        }
        int a = 0;
        System.gc();
    }
}
