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



