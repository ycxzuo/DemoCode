package com.yczuoxin.jvm.demo.classloader.init;

import com.yczuoxin.jvm.demo.classloader.bean.SubClass;

/**
 *  -XX:+TraceClassLoading
 *  虚拟机把描述类的数据从 Class 文件加载到内存，并对数据进行校验、转换解析和初始化，最终形成可以被虚拟机直接使用的 Java 类型，这就是虚拟机的类加载机制
 *
 * 类从被加载到虚拟机内存中开始，到卸载出内存位置，它的整个生命周期包括：
 *
 * - 加载（Loading）
 * - 验证（Verification）
 * - 准备（Preparation）
 * - 解析（Resolution）
 * - 初始化（Initialization）
 * - 使用（Using）
 * - 卸载（Unloading）
 *
 * 其中验证、准备、解析 3 个部分统称为连接（Linking）
 *
 * 加载、验证、准备、初始化和卸载这五个阶段的顺序是确定的，类的加载过程必须按照这种顺序开始，解析阶段则不一定，
 * 他在某些情况下可以在初始化阶段之后再开始（这里说“开始”而不用“进行”或“完成”是因为这些阶段通常都是互相交叉的混合式进行的，
 * 通常会在一个阶段执行的过程中调用、激活另外一个阶段），这是为了支持 Java 语言的运行时绑定（也称为动态绑定或晚期绑定）。
 *
 * Java 虚拟机规范中没有强行约束什么情况下开始加载步骤，这点可以直接交给虚拟机的具体实现来自由把握。
 * 但对于初始化阶段，虚拟机规范则是严格规定了有且只有 5 种情况必须立即对类进行
 * “初始化”（而加载、验证、准备自然需要在此之前开始）
 *
 * - 遇到new/ getstatic/ putstatic/ invokestatic这 4 条字节码指令时，如果类没有进行过初始化，则需要先触发其初始化。
 *   生成这 4 条指令的最常见的 Java 代码场景是
 *   - 使用new关键字实例化对象的时候
 *   - 读取或设置一个类的静态字段（被final修饰、已在编译期把结果放入常量池的静态字段除外）的时候
 *   - 调用一个类的静态方法的时候
 * - 使用java.lang.reflect包的方法对类进行反射调用的时候，如果类没有进行过初始化，则需要先出法其初始化
 * - 当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化
 * - 当虚拟机启动时，用户需要指定一个要执行的主类（包含main()方法的那个类），虚拟机会先初始化这个主类
 * - 当使用JDK1.7的动态语言支持时，如果一个java.lang.invoke.MethodHandle实例最后的解析结果REF_getStatic/ REF_putStatic/ REF_invokeStatic的方法句柄，
 *   并且这个方法句柄所对应的类没有进行过初始化，则需要先触发其初始化
 *
 * 这 5 种场景中的行为称为对一个类型性主动引用。除此之外，所有引用类的方式都不会触发初始化，称为被动引用。
 * 下面代码只会输出SuperClass 被初始化，对于静态字段，只有直接定义这个字段的类才会被初始化，但是利用-XX:+TraceClassLoading监控类的加载发现子类被加载了
 */
public class InitDemo1 {
    public static void main(String[] args) {
        System.out.println(SubClass.value);
    }
}
