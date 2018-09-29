package me.seriouszyx.prime;

import me.seriouszyx.base.Bird;
import org.junit.Test;

public class TestPrime {

    @Test
    public void testBird2() {
        Bird2 bird2 = new Bird2();
        bird2.fly();
    }

    @Test
    public void testBird3() {
        Bird bird = new Bird();
        Bird3 bird3 = new Bird3(bird);
        bird3.fly();
    }



}
