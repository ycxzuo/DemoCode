package com.yczuoxin.jvm.demo.methodinvoke.dispatch;

/**
 * 单分派和多分派
 *  宗量
 *      方法的接收者和方法的参数统称为方法的宗量
 *  单分派
 *      只有一个宗量作为选择依据，所以 Java 语言的动态分派属于单分派类型
 *  多分派
 *      根据两个宗量进行选择，所以 Java 语言的静态分派属于多分派类型
 * 编译阶段编译器的选择过程（多分派）
 *  - 静态类型是Father还是Son
 *  - 方法参数是QQ还是360
 *  产生了两条invokevirtual指令，
 *  两条指令的参数分别为常量池中指向Father.hardChoice(360)及Father.hardChoice(QQ)方法的符号引用
 * 运行阶段虚拟机的选择过程（单分派）
 *  由于编译期已经决定目标方法的签名必须为hardChoice(QQ)，
 *  虚拟机此时不会关心传递过来的参数QQ到底是腾讯QQ还是奇瑞QQ，
 *  因为这时参数的静态类型、实际类型都对方法的选择不会构成任何影响，
 *  唯一可以影响虚拟机选择的因素只有此方法的接收者的实际类型是Father还是Son
 * @author yczuoxin
 */
public class Dispatch {
    static class QQ {}
    static class _360 {}

    public static class Father {
        public void hardChoice(QQ arg){
            System.out.println("father choose qq");
        }

        public void hardChoice(_360 arg){
            System.out.println("father choose 360");
        }
    }

    public static class Son extends Father {
        @Override
        public void hardChoice(QQ arg){
            System.out.println("son choose qq");
        }

        @Override
        public void hardChoice(_360 arg){
            System.out.println("son choose 360");
        }
    }

    public static void main(String[] args) {
        Father father = new Father();
        Father son = new Son();
        father.hardChoice(new _360());
        son.hardChoice(new QQ());
    }
}
