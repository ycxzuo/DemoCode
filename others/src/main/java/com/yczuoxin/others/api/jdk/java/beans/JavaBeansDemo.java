package com.yczuoxin.others.api.jdk.java.beans;

import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.EventSetDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.stream.Stream;

public class JavaBeansDemo {

    public static void main(String[] args) throws IntrospectionException {
        // 根据 Introspector 获取 Bean Info 信息
        // 第一个参数是需要内省类，第二个是到那个父类停止的标志
        BeanInfo beanInfo = Introspector.getBeanInfo(Person.class, Object.class);
        // 根据 BeanInfo 获取 Bean Descriptor
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        System.out.println(beanDescriptor);
        System.out.println("------------------------------------");
        // 根据 BeanInfo 获取 Bean Event
        EventSetDescriptor[] eventSetDescriptors = beanInfo.getEventSetDescriptors();
        printStream(Stream.of(eventSetDescriptors));

        // 根据 BeanInfo 获取 Bean Method Descriptor
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
        printStream(Stream.of(methodDescriptors));
        Arrays.stream(methodDescriptors).forEach(method -> {
            System.out.println("method: " + method.getMethod());
            System.out.println(method.getName());
            System.out.println(method.getDisplayName());
            System.out.println("----------------------------------");
        });

        // 根据 BeanInfo 获取 Bean PropertyDescriptor
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        printStream(Stream.of(propertyDescriptors));
        Arrays.stream(propertyDescriptors).forEach(property -> {
            System.out.println("readable method");
            System.out.println(property.getReadMethod().toString());
            System.out.println("writable method");
            System.out.println(property.getWriteMethod().toString());
        });
    }

    private static<T> void printStream(Stream<T> stream) {
        stream.forEach(System.out::println);
        System.out.println("------------------------------------");
    }
}
