package me.seriouszyx.IoCFactoryImpl;

import me.seriouszyx.IoCFactory.ApplicationContext;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @ClassName ClassPathXMLApplicationContext
 * @Description ApplicationContext的实现，核心类
 * @Author Yixiang Zhao
 * @Date 2018/9/22 9:40
 * @Version 1.0
 */
public class ClassPathXMLApplicationContext implements ApplicationContext {

    private Map map = new HashMap();

    public ClassPathXMLApplicationContext(String location) {
        try {
            Document document = getDocument(location);
            XMLParsing(document);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 加载资源文件，转换成Document类型
    private Document getDocument(String location) throws JDOMException, IOException {
        SAXBuilder saxBuilder = new SAXBuilder();
        return saxBuilder.build(this.getClass().getClassLoader().getResource(location));
    }

    private void XMLParsing(Document document) throws Exception {
        // 获取XML文件根元素beans
        Element beans = document.getRootElement();
        // 获取beans下的bean集合
        List beanList = beans.getChildren("bean");
        // 遍历beans集合
        for (Iterator iter = beanList.iterator(); iter.hasNext(); ) {
            Element bean = (Element) iter.next();
            // 获取bean的属性id和class，id为类的key值，class为类的路径
            String id = bean.getAttributeValue("id");
            String className = bean.getAttributeValue("class");
            // 动态加载该bean代表的类
            Object obj = Class.forName(className).newInstance();
            // 获得该类的所有方法
            Method[] methods = obj.getClass().getDeclaredMethods();
            // 获取该节点的所有子节点，子节点存储类的初始化参数
            List<Element> properties = bean.getChildren("property");
            // 遍历，将初始化参数和类的方法对应，进行类的初始化
            for (Element pro : properties) {
                for (int i = 0; i < methods.length; i++) {
                    String methodName = methods[i].getName();
                    if (methodName.startsWith("set")) {
                        String classProperty = methodName.substring(3, methodName.length()).toLowerCase();
                        if (pro.getAttribute("name") != null) {
                            if (classProperty.equals(pro.getAttribute("name").getValue())) {
                                methods[i].invoke(obj, pro.getAttribute("value").getValue());
                            }
                        } else {
                            methods[i].invoke(obj, map.get(pro.getAttribute("ref").getValue()));
                        }
                    }
                }
            }
            // 将初始化完成的对象添加到HashMap中
            map.put(id, obj);
        }
    }

    public Object getBean(String name) {
        return map.get(name);
    }

}

