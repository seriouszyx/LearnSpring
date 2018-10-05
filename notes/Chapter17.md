#   Chapter 17

##  DispatcherServlet

DispatcherServlet 是 Spring MVC 的灵魂，它负责接收 HTTP 请求并协调 Spring MVC 的各个组件完成请求处理的工作。

要了解 Spring MVC 框架的工作机理，必须理解以下三个问题：

*   DispatcherServlet 框架如何截获特定的 HTTP 请求并交由 Spring MVC 框架处理？
*   位于 Web 层的 Spring 容器（WebApplicationContext）图和与位于业务层的 Spring 容器（ApplicationContext）建立关联，以使 Web 层的 Bean 可以调用业务层的 Bean。
*   如何初始化 Spring MVC 的各个组件，并将它们装配到 DispatcherServlet 中。



配置时，需要在 web.xml 文件中加入以下代码：

```xml
	<servlet>
		<servlet-name>smart</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>smart</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>

```

其中，默认配置文件位于 `WEB-INF/<servlet-name>-servlet.xml`。

```xml
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:/applicationContext.xml</param-value>
	</context-param>
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
```

在这里，“Web 层”Spring 容器将作为“业务层”Spring 容器的子容器，即“Web 层”容器可以引用“业务层”容器的 Bean，而“业务层”容器却访问不到“Web 层”容器的 Bean。

```java
    protected void initStrategies(ApplicationContext context) {
        this.initMultipartResolver(context);    // 初始化上传文件解析器（直译为多部分请求解析器）
        this.initLocaleResolver(context);   // 初始化本地化解析器
        this.initThemeResolver(context);    // 初始化主题解析器
        this.initHandlerMappings(context);  // 初始化处理器映射器
        this.initHandlerAdapters(context);  // 初始化处理器适配器
        this.initHandlerExceptionResolvers(context);    // 初始化处理器异常解析器
        this.initRequestToViewNameTranslator(context);  // 初始化请求到视图名翻译器
        this.initViewResolvers(context);    // 初始化视图解析器
        this.initFlashMapManager(context);
    }
```

initStrategies() 方法将在 WebApplicationContext 初始化后自动执行，此时 Spring 上下文中的 Bean 已经初始化完毕。该方法的工作原理是：通过反射机制查找并装配 Spring 容器中用户显示自定义的组件 Bean，如果找不到，则装配默认的组件实例。


## Spring MVC 一般流程

下面实现一个用户登录功能来说明 Spring MVC 的工作流程，源码已发在我的[GitHub](https://github.com/seriouszyx/LearnSpring/tree/master/mycode/MVC-Demo)。

1.	配置 `web.xml`。

*	ContextLoaderListener 和 DispatcherServlet

任何 web 框架都离不开 servlet，想和 web 搞上关系，就要依赖 servlet，listner 或 filter, 最终都是为了切进 ServletContext。

ContextLoaderListener 的作用就是启动Web容器时，自动装配 ApplicationContext 的配置信息。因为它实现了 ServletContextListene r这个接口，在 web.xm l配置这个监听器，启动容器时，就会默认执行它实现的方法。

DispatcherServlet 将 web 请求转发给 controller 层处理，它与 ContextLoaderListener 构成父子容器，下面是两者的关系图。

![ContextLoaderListener 和 DispatcherServlet 关系](https://upload-images.jianshu.io/upload_images/3362699-d4f492808be52ebe.jpg)

监听器+servlet 的组合，两个容器各司其职。父上下文做核心容器，子上下文处理 web 相关。

2.	配置 `ApplicationContext.xml`

ApplicationContext.xml 作为 IoC 容器的配置文件，记录了一些 bean 的初始化信息，比如用注解的方式导入 bean，需要在配置文件中配置需要扫描的包。

3.	配置 `<servlet-name>-servlet.xml`

这里的配置文件可以对视图文件（如freemarker、jsp等）进行初始化操作，如定义视图文件的位置，定义全局变量、定义拦截器等等。

4.	实现 jsp

jsp 发送请求时可以参照 web.xml 中关于 servlet 的拦截信息，比如这个 demo 中就可以`.html` 作为后缀。

5.	实现 controller


##	注解驱动的控制器

###	@RequestMapping

@RequestMapping 在类定义出指定的 URL  相对于 Web 应用的部署路径，而在方法定义处指定的 URL 则相对于类定义出指定的 URL。

@RequestMapping 不但支持标准的 URL，还支持 `Ant` 风格和带 `{xxx}` 占位符的 URL。

通过 `@PathVariable` 可以将 URL 中的占位符参数绑定到控制器处理方法的入参中（路径参数），最好在 @PathVariable 中显示指定绑定的参数名，比如：

```java
	@RequestMapping("/user/{userId}")
	public ModelAndView showDetail(@PathVariable("userId") String userId) {
		...
	}
```

HTTP 请求报文除 URL 外，还拥有其他众多信息，@RequestMapping 也有与之映射的方法。

|参数名|表示含义|
|:--:|:--:|
|value|请求URL|
|method|请求方法|
|params|请求参数|
|headers|报文头的映射信息|

比如：

```java
	@RequestMapping(path="/delete", method=RequestMethod.POST, params="userId")
	public String test1(@RequestParam("userId") String userId) {
		...
	}
```

它们之间是与的关系，联合使用多个条件项可让请求映射更加精确化。


###	请求处理方法签名

Spring MVC 通过分析处理方法的签名，将 HTTP 请求信息绑定到处理方法的相应入参中，然后再调用处理方法得到返回值，最后对返回值进行处理并返回响应。

一般情况下，处理方法的返回值类型为 ModelAndView 或 String，前者包含模型和逻辑视图名，后者仅代表一个逻辑视图名。

###	使用 HttpMessageConverter<T>






参考文章：

[SpringMVC中为什么要配置Listener和Servlet](https://blog.csdn.net/fcs_learner/article/details/74085053)

[SpringMVC启动过程详解](https://www.jianshu.com/p/843576c42ba1)
