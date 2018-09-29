package me.seriouszyx.proxy;

import me.seriouszyx.base.Flyable;

/**
 * @ClassName BirdTimeProxy
 * @Description 代理类记录飞行时间
 * @Author Yixiang Zhao
 * @Date 2018/9/29 9:36
 * @Version 1.0
 */
public class BirdTimeProxy implements Flyable {

    private Flyable flyable;

    public BirdTimeProxy(Flyable flyable) {
        this.flyable = flyable;
    }

    @Override
    public void fly() {
        long start  = System.currentTimeMillis();

        flyable.fly();

        long end = System.currentTimeMillis();
        System.out.println("Fly time = " + (end - start));
    }
}
