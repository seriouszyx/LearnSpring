#   Chapter 4

原生的 JavaEE 技术中各个模块之间的联系较强，即耦合度较高。

比如完成一个用户的创建事务，视图层会创建业务逻辑层的对象，再在内部调用对象的方法，各个模块的独立性很差，如果某一模块的代码发生改变，其他模块的改动也会很大。

而 Spring 框架的核心——IoC（控制反转）很好的解决了这一问题。控制反转，即某一接口具体实现类的选择控制权从调用类中移除，转交给第三方决定，即由 Spring 容器借由 Bean 配置来进行控制。

可能 IoC 不够开门见山，理解起来较为困难。因此， Martin Fowler 提出了 DI（Dependency Injection，依赖注入）的概念来替代 IoC，即让调用类对某一接口实现类的依赖关系由第三方（容器或写协作类）注入，以移除调用类对某一接口实现类的依赖。

比如说， 上述例子中，视图层使用业务逻辑层的接口变量，而不需要真正 new 出接口的实现，这样即使接口产生了新的实现或原有实现修改，视图层都能正常运行。

从注入方法上看，IoC 主要划分为三种类型：构造函数注入、属性注入和接口注入。在开发过程中，一般使用属性注入的方法。

IoC 不仅可以实现类之间的解耦，还能帮助完成类的初始化与装配工作，让开发者从这些底层实现类的实例化、依赖关系装配等工作中解脱出出来，专注于更有意义的业务逻辑开发工作。

下面实现了一个IoC容器的核心部分，简单模拟了IoC容器的基本功能。

特别说明一点：spring IoC 中使用懒加载机制，在启动 spring IoC 时，只会实例化单例模式的 bean，不会实例化普通的 bean。不过为了简便，我的实现在容器启动的时候就将所有的 bean 进行初始化。

下面列举出核心类：

Student.java

```java
/**
 * @ClassName Student
 * @Description 学生实体类
 * @Author Yixiang Zhao
 * @Date 2018/9/22 9:19
 * @Version 1.0
 */
public class Student {

    private String name;

    private String gender;

    public void intro() {
        System.out.println("My name is " + name + " and I'm " + gender + " .");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
```

StuService.java

```java
/**
 * @ClassName StuService
 * @Description 学生Service
 * @Author Yixiang Zhao
 * @Date 2018/9/22 9:21
 * @Version 1.0
 */
public class StuService {

    private Student student;

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
}
```

beans.xml

```java
<?xml version="1.0" encoding="UTF-8"?>

<beans>
    <bean id="Student" class="me.seriouszyx.pojo.Student">
        <property name="name" value="ZYX"/>
        <property name="gender" value="man"/>
    </bean>

    <bean id="StuService" class="me.seriouszyx.service.StuService">
        <property ref="Student"/>
    </bean>
</beans>
```

下面是核心类 ClassPathXMLApplicationContext.java

```java

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
```

最后进行测试

```java
public class MyIoCTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXMLApplicationContext("beans.xml");
        StuService stuService = (StuService) context.getBean("StuService");
        stuService.getStudent().intro();
    }
}
```

测试成功！

```text
E:\JDK8\bin\java.exe "-javaagent:E:\IntelliJ IDEA 2018.1.5\lib\idea_rt.jar=1993:E:\IntelliJ IDEA 2018.1.5\bin" -Dfile.encoding=UTF-8 -classpath E:\JDK8\jre\lib\charsets.jar;E:\JDK8\jre\lib\deploy.jar;E:\JDK8\jre\lib\ext\access-bridge-64.jar;E:\JDK8\jre\lib\ext\cldrdata.jar;E:\JDK8\jre\lib\ext\dnsns.jar;E:\JDK8\jre\lib\ext\jaccess.jar;E:\JDK8\jre\lib\ext\jfxrt.jar;E:\JDK8\jre\lib\ext\localedata.jar;E:\JDK8\jre\lib\ext\nashorn.jar;E:\JDK8\jre\lib\ext\sunec.jar;E:\JDK8\jre\lib\ext\sunjce_provider.jar;E:\JDK8\jre\lib\ext\sunmscapi.jar;E:\JDK8\jre\lib\ext\sunpkcs11.jar;E:\JDK8\jre\lib\ext\zipfs.jar;E:\JDK8\jre\lib\javaws.jar;E:\JDK8\jre\lib\jce.jar;E:\JDK8\jre\lib\jfr.jar;E:\JDK8\jre\lib\jfxswt.jar;E:\JDK8\jre\lib\jsse.jar;E:\JDK8\jre\lib\management-agent.jar;E:\JDK8\jre\lib\plugin.jar;E:\JDK8\jre\lib\resources.jar;E:\JDK8\jre\lib\rt.jar;F:\masterspring\mycode\SimpleIoC\out\production\SimpleIoC;F:\masterspring\mycode\SimpleIoC\src\me\seriouszyx\lib\jdom-1.1.3.jar me.seriouszyx.test.MyIoCTest
My name is ZYX and I'm man .

Process finished with exit code 0
```

