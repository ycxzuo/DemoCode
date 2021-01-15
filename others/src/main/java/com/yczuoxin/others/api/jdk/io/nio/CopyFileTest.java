package com.yczuoxin.others.api.jdk.io.nio;

import java.io.IOException;

public class CopyFileTest {
    public static void main(String[] args) throws IOException {
        CopyFileUtil.copyFile("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\timg.jpg", "D:\\tmp\\t.jpg");
    }
}
