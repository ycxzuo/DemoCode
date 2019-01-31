package com.yczuoxin.jvm.demo.methodinvoke.dynamictypelanguage;

/**
 * 这段代码编译期不会报错
 * 运行期会抛出 java.lang.NegativeArraySizeException 这是一个运行时异常
 * @author yczuoxin
 */
public class Demo {
    public static void main(String[] args) {
        int[][][] array = new int[1][0][-1];
    }
}
