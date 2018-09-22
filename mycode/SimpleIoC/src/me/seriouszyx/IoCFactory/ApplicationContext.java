package me.seriouszyx.IoCFactory;

/**
 * @InterfaceName ApplicationContext
 * @Description ApplicationContext接口，可以通过它来获得配置的对象。
 * @Author Yixiang Zhao
 * @Date 2018/9/22 10:50
 * @Version 1.0
 */
public interface ApplicationContext {

    public Object getBean(String name);


}
