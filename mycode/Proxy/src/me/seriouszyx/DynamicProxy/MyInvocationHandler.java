package me.seriouszyx.DynamicProxy;

import me.seriouszyx.base.Bird;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName MyInvocationHandler
 * @Description 用户自定义的代理逻辑处理
 * @Author Yixiang Zhao
 * @Date 2018/9/29 16:03
 * @Version 1.0
 */
public class MyInvocationHandler implements InvocationHandler {
        private Bird bird;

        public MyInvocationHandler(Bird bird) {
            this.bird = bird;
        }

        @Override
        public void invoke(Object proxy, Method method, Object[] args) {
            long start = System.currentTimeMillis();

            try {
                method.invoke(bird, new Object[] {});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }

            long end = System.currentTimeMillis();
            System.out.println("Fly time = " + (end - start));
        }

}
