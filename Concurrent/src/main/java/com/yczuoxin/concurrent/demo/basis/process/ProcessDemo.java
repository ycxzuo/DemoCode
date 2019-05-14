package com.yczuoxin.concurrent.demo.basis.process;

import java.io.IOException;

public class ProcessDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec("cmd /k start http://www.baidu.com");
        Thread.sleep(1000);
        process.destroy();
    }
}
