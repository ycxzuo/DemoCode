package com.yczuoxin.jvm.demo.methodinvoke.methoddispatchrule;

public class Test {
    public static void main(String[] args) {
        (new Test().new Son()).thinking();
    }

    class GrandFather {
        void thinking() {
            System.out.println("I am grandfather");
        }
    }

    class Father extends GrandFather {
        @Override
        void thinking() {
            System.out.println("I am father");
        }
    }

    class Son extends Father {
        @Override
        void thinking() {
            /*try {
                MethodType mt = MethodType.methodType(void.class);
				MethodHandle mh = MethodHandles.lookup().findSpecial(GrandFather.class, "thinking", mt, getClass());
				mh.invoke(this);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }*/
        }
    }
}
