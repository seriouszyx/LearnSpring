package me.seriouszyx.proxy;

import me.seriouszyx.base.Bird;
import org.junit.Test;

public class TestProxy {

    @Test
    public void testProxy() {
        Bird bird = new Bird();
        BirdLogProxy p1 = new BirdLogProxy(bird);
        BirdTimeProxy p2 = new BirdTimeProxy(p1);
        p2.fly();
    }

}
