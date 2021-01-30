package com.yczuoxin.others.spring.proxy.my;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

public class Proxy {

    public static Object newProxyInstance(Class interfaceClazz, InvocationHandler h) throws Exception {
        StringBuilder methodStr = new StringBuilder();
        String rt = "\r\n";

        Method[] methods = interfaceClazz.getMethods();
        for (Method method : methods) {
            methodStr.append("@Override").append(rt)
                    .append(" public void ").append(method.getName()).append("() {").append(rt)
                    .append("   try {").append(rt)
                    .append("   Method md = ").append(interfaceClazz.getName()).append(".class.getMethod(\"").append(method.getName()).append("\");").append(rt)
                    .append("   h.invoke(this, md);").append(rt)
                    .append("   } catch(Exception e) {").append(rt)
                    .append("       e.printStackTrace();").append(rt)
                    .append("   }").append(rt)
                    .append("}").append(rt);
        }
        String src =
                "package com.yczuoxin.others.spring.proxy.my;" + rt +
                "import java.lang.reflect.Method;" + rt +
                "public class $Proxy1 implements " + interfaceClazz.getName() + "{" + rt +
                "   public $Proxy1 (InvocationHandler h) {" + rt +
                "       this.h = h;" + rt +
                "   }" + rt +
                "com.yczuoxin.others.spring.proxy.my.InvocationHandler h;" + rt +
                methodStr + "}";

        String fileName = System.getProperty("user.dir") + "/others/src/main/java/com/yczuoxin/others/spring/proxy/my/$Proxy1.java";
        File f = new File(fileName);
        FileWriter fw = new FileWriter(f);
        fw.write(src);
        fw.flush();
        fw.close();

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
        Iterable<? extends JavaFileObject> units = fileManager.getJavaFileObjects(fileName);
        JavaCompiler.CompilationTask t = compiler.getTask(null, fileManager, null, null, null, units);
        t.call();
        fileManager.close();

        URL[] urls = new URL[]{new URL("file:/" + System.getProperty("user.dir") + "/others/src/main/java/")};

        URLClassLoader ul = new URLClassLoader(urls);
        Class<?> c = ul.loadClass("com.yczuoxin.others.spring.proxy.my.$Proxy1");
        System.out.println(c);
        Constructor<?> constructor = c.getConstructor(InvocationHandler.class);
        Object method = constructor.newInstance(h);
        return method;
    }

}
