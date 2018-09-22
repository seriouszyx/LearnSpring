#   Spring IoC容器浅析及简单实现

<!-- TOC -->

- [Spring IoC 概述](#spring-ioc-概述)
- [Spring IoC 简单实现](#spring-ioc-简单实现)
- [BeanFactory 和 ApplicationContext](#beanfactory-和-applicationcontext)
- [WebApplicationContext](#webapplicationcontext)

<!-- /TOC -->

##	Spring IoC 概述

原生的 JavaEE 技术中各个模块之间的联系较强，即`耦合度较高`。

比如完成一个用户的创建事务，视图层会创建业务逻辑层的对象，再在内部调用对象的方法，各个模块的`独立性很差`，如果某一模块的代码发生改变，其他模块的改动也会很大。

而 Spring 框架的核心——IoC（控制反转）很好的解决了这一问题。控制反转，即`某一接口具体实现类的选择控制权从调用类中移除，转交给第三方决定`，即由 Spring 容器借由 Bean 配置来进行控制。

可能 IoC 不够开门见山，理解起来较为困难。因此， Martin Fowler 提出了 DI（Dependency Injection，依赖注入）的概念来替代 IoC，即`让调用类对某一接口实现类的依赖关系由第三方（容器或写协作类）注入，以移除调用类对某一接口实现类的依赖`。

比如说， 上述例子中，视图层使用业务逻辑层的接口变量，而不需要真正 new 出接口的实现，这样即使接口产生了新的实现或原有实现修改，视图层都能正常运行。

从注入方法上看，IoC 主要划分为三种类型：构造函数注入、属性注入和接口注入。在开发过程中，一般使用`属性注入`的方法。

IoC 不仅可以实现`类之间的解耦`，还能帮助完成`类的初始化与装配工作`，让开发者从这些底层实现类的实例化、依赖关系装配等工作中解脱出出来，专注于更有意义的业务逻辑开发工作。

##	Spring IoC 简单实现

下面实现了一个IoC容器的核心部分，简单模拟了IoC容器的基本功能。

特别说明一点：spring IoC 中使用`懒加载`机制，在启动 spring IoC 时，只会实例化单例模式的 bean，不会实例化普通的 bean。不过为了简便，我的实现`在容器启动的时候就将所有的 bean 进行初始化`。

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

```xml
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
My name is ZYX and I'm man .

Process finished with exit code 0
```

##  BeanFactory 和 ApplicationContext

Spring的IoC容器不仅仅提供了上述的一些底层工作，还提供了 Bean 实例缓存、生命周期管理、Bean 实例代理、时间发布、资源装载等高级服务。

Bean 工厂是 Spring 框架最核心的接口，提供了该记得 IoC 配置机制。然而，在开发中一般不去直接使用 BeanFactory，它是 Spring 框架的基础设施，面向 Spring 本身；而ApplicationContext 面向使用 Spring 框架的开发者，几乎所有的应用场合都可以直接使用 ApplicationContext 而非底层的 BeanFactory。

BeanFactory 中建议使用 XmlBeanDefinitionReader、DefaultListableBeanFactory 两个实现类。

接口中最主要的方法就是 getBean(String beanName)，该方法从容器中返回特定名称的 Bean.

需要注意的是，在初始化 BeanFactory 时，必须为其提供一种日志框架，一般使用 Loag4J，这样启动 Spring 容器才不会报错。


ApplicationContext 由 BeanFactory 派生而来，可以通过配置实现更多面向实际应用的功能。

两者在初始化容器时有一个显著区别：BeanFactory 在初始化容器时并未实例化 Bean，直到第一次访问某个 Bean 时才实例化目标 Bean；而 ApplicationContext 则在初始化应用上下文时就实例化所有单实例的 Bean。

ApplicationContext 的主要实现类是 ClassPathXmlApplicationContext 和 FileSystemXmlApplicationContext，区别是装载配置路径的方式不同，而且它们可以接受多个配置文件的路径。

Spring 同样支持基于类注解的配置方式，一个标注@Configuration 注解的 POJO 即可提供 Spring 所需的 Bean 配置信息。

Spring4.0 支持使用 Groovy DSL 来进行 Bean 的定义配置，基于 Groovy 脚本语言，可以实现复杂、灵活的 Bean 配置逻辑，实现类为 GenericGroovyApplicationContext，推荐使用。


##  WebApplicationContext

WebApplicationContext 类专门为 Web 应用准备，由于 web 应用比一般的应用拥有更多的特性，因此 WebApplicationContext 扩展了 ApplicationContext。

因为 WebApplicationContext 需要 ServletContext 实例，它必须在拥有 Web 容器的前提下才能完成启动工作，因此，借助 Servlet 和 ServletContextListener 二者中的任何一个，都能完成启动 Spring Web 应用上下文的工作。

同样，可以使用 XML、注解和 Groovy DSL 三种方式来提供配置信息。

一般在容器初始化前，要先配置日志框架，可以选择将 Log4J 的未知文件防止在类路径 WEB-INF/classes 下，注意在 XML 中将 log4jConfigServlet 的启动顺序号设置为1。
