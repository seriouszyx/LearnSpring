package me.seriouszyx.DynamicProxy;

import me.seriouszyx.base.Bird;
import me.seriouszyx.base.Flyable;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @ClassName TestProxy
 * @Description 测试动态代理类
 * @Author Yixiang Zhao
 * @Date 2018/9/29 15:12
 * @Version 1.0
 */
public class TestProxy {

    @Test
    public void testProxy() {
        try {
            Proxy.newProxyInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJavaCompile() {
        try {
            // 运行成功后可在该包下得到.class文件
            JavaCompiler.compile(new File("../Proxy/src/me/seriouszyx/DynamicProxy/TimeProxy.java"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNewInstance() {
        try {
            URL[] urls = new URL[] {new URL("file:/../Proxy/src")};
            URLClassLoader classLoader = new URLClassLoader(urls);
            Class clazz = null;
            clazz = classLoader.loadClass("me.seriouszyx.DynamicProxy.TimeProxy");
            Constructor constructor = clazz.getConstructor(Flyable.class);
            Flyable flyable = (Flyable) constructor.newInstance(new Bird());
            flyable.fly();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testBetterProxy() {
        try {
            Flyable flyable = (Flyable) BetterProxy.newProxyInstance(Flyable.class, new MyInvocationHandler(new Bird()));
            flyable.fly();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
