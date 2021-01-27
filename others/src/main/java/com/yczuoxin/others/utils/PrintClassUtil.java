package com.yczuoxin.others.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * 输出 Class 的字节码数据
 */
public class PrintClassUtil {

    private PrintClassUtil() {
    }

    private static String getModifier(int modifier) {
        String result = "";
        switch (modifier) {
            case Modifier.PRIVATE:
                result = "private";
                break;
            case Modifier.PUBLIC:
                result = "public";
                break;
            case Modifier.PROTECTED:
                result = "protected";
                break;
            case Modifier.ABSTRACT:
                result = "abstract";
                break;
            case Modifier.FINAL:
                result = "final";
                break;
            case Modifier.NATIVE:
                result = "native";
                break;
            case Modifier.STATIC:
                result = "static";
                break;
            case Modifier.SYNCHRONIZED:
                result = "synchronized";
                break;
            case Modifier.STRICT:
                result = "strict";
                break;
            case Modifier.TRANSIENT:
                result = "transient";
                break;
            case Modifier.VOLATILE:
                result = "volatile";
                break;
            case Modifier.INTERFACE:
                result = "interface";
                break;
            default:
                break;
        }
        return result;
    }

    public static void printClassDefinition(Class<?> clz) {

        String clzModifier = getModifier(clz.getModifiers());
        if (clzModifier != null && !clzModifier.equals("")) {
            clzModifier = clzModifier + " ";
        }
        String superClz = clz.getSuperclass().getName();
        if (!superClz.equals("")) {
            superClz = "extends " + superClz;
        }

        Class<?>[] interfaces = clz.getInterfaces();

        StringBuilder inters = new StringBuilder();
        for (int i = 0; i < interfaces.length; i++) {
            if (i == 0) {
                inters.append("implements ");
            }
            inters.append(interfaces[i].getName());
        }

        System.out.println(clzModifier + clz.getName() + " " + superClz + " " + inters);
        System.out.println("{");

        Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String modifier = getModifier(field.getModifiers());
            if (modifier != null && !modifier.equals("")) {
                modifier = modifier + " ";
            }
            String fieldName = field.getName();
            String fieldType = field.getType().getName();
            System.out.println("    " + modifier + fieldType + " " + fieldName + ";");
        }

        System.out.println();

        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            String modifier = getModifier(method.getModifiers());
            if (modifier != null && !modifier.equals("")) {
                modifier = modifier + " ";
            }

            String methodName = method.getName();

            Class<?> returnClz = method.getReturnType();
            String retrunType = returnClz.getName();

            Class<?>[] clzs = method.getParameterTypes();
            StringBuilder paraList = new StringBuilder("(");
            for (int j = 0; j < clzs.length; j++) {
                paraList.append(clzs[j].getName());
                if (j != clzs.length - 1) {
                    paraList.append(", ");
                }
            }
            paraList.append(")");

            clzs = method.getExceptionTypes();
            StringBuilder exceptions = new StringBuilder();
            for (int j = 0; j < clzs.length; j++) {
                if (j == 0) {
                    exceptions.append("throws ");
                }

                exceptions.append(clzs[j].getName());

                if (j != clzs.length - 1) {
                    exceptions.append(", ");
                }
            }

            exceptions.append(";");

            String methodPrototype = modifier + retrunType + " " + methodName + paraList + exceptions;

            System.out.println("    " + methodPrototype);

        }
        System.out.println("}");
    }
}
