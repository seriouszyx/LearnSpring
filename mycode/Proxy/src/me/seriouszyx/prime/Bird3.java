package me.seriouszyx.prime;

import me.seriouszyx.base.Bird;
import me.seriouszyx.base.Flyable;

/**
 * @ClassName Bird3
 * @Description 使用聚合方法
 * @Author Yixiang Zhao
 * @Date 2018/9/29 9:25
 * @Version 1.0
 */
public class Bird3 implements Flyable {

    private Bird bird;

    public Bird3(Bird bird) {
        this.bird = bird;
    }

    @Override
    public void fly() {
        long start = System.currentTimeMillis();

        bird.fly();

        long end = System.currentTimeMillis();
        System.out.println("Fly time = " + (end - start));
    }
}
