package me.seriouszyx.test;

import me.seriouszyx.IoCFactory.ApplicationContext;
import me.seriouszyx.IoCFactoryImpl.ClassPathXMLApplicationContext;
import me.seriouszyx.service.StuService;

/**
 * @ClassName MyIoCTest
 * @Description 测试自定义的IoC
 * @Author Yixiang Zhao
 * @Date 2018/9/22 10:51
 * @Version 1.0
 */
public class MyIoCTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXMLApplicationContext("beans.xml");
        StuService stuService = (StuService) context.getBean("StuService");
        stuService.getStudent().intro();
    }
}
