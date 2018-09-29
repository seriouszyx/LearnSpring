package me.seriouszyx.proxy;

import me.seriouszyx.base.Flyable;

/**
 * @ClassName BirdLogProxy
 * @Description 代理类记录飞行日志
 * @Author Yixiang Zhao
 * @Date 2018/9/29 9:39
 * @Version 1.0
 */
public class BirdLogProxy implements Flyable {

    private Flyable flyable;

    public BirdLogProxy(Flyable flyable) {
        this.flyable = flyable;
    }

    @Override
    public void fly() {
        System.out.println("Bird fly start...");

        flyable.fly();

        System.out.println("Bird fly end...");
    }

}
