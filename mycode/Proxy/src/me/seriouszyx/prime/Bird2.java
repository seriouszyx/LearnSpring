package me.seriouszyx.prime;

import me.seriouszyx.base.Bird;

/**
 * @ClassName Bird2
 * @Description 使用继承方法
 * @Author Yixiang Zhao
 * @Date 2018/9/29 9:16
 * @Version 1.0
 */
public class Bird2 extends Bird {

    @Override
    public void fly() {
        long start = System.currentTimeMillis();

        super.fly();

        long end = System.currentTimeMillis();
        System.out.println("Fly time = " + (end - start));
    }
}
