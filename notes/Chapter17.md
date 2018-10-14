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

###	处理模型数据

*	ModelAndView

控制器处理方法返回值如果为 ModelAndView，则其既包含视图信息，又包含模型处理信息，这样 Spring MVC 就可以使用视图对模型数据进行渲染。

*	@ModelAttribute

可以在方法或方法参数中进行注解。在方法上使用 @ModelAttribute，则在每个处理方法调用前，都会调用这个方法。

```Java
	@ModelAttribute
	public User getUser() {
		User user = new User();
		user.setUserId("1001");
		return user;
	}

	@RequestMapping(path = "/handle")
	public String handle(@ModelAttribute("user") User user) {
		user.setUserName("tom");
		return "/user/showUser";
	}
```

*	Map 及 Model

Spring MVC 在调用方法前会创建一个隐含的模型对象，作为模型数据的存储容器，我们称之为“隐含模型”。如果处理方法的入参为 Map 或 Model 类型，则 Spring MVC 会将隐含模型的引用传递给这些入参，开发者可以通过这个入参对象访问到模型中的所有数据。

```Java
		@ModelAttribute
		public User getUser() {
			User user = new User();
			user.setUserId("1001");
			return user;
		}

		@RequestMapping(path = "/handle")
		public String handle(ModelMap modelmap) {
			modelMap.addAttribute("testArr", "value");
			User user = (User)modelMap.get("user");
			user.setUserName("tom");
			return "/user/showUser";
		}
```

*	@SessionAttributes

在类前注解声明会向隐含模型中添加一个名为 user 的模型对象。

不过在会话中找不到对应的属性，则抛出 HttpSessionRequiredException 异常。最好在方法上使用 @ModelAttribute 向隐含模型中添加的模型属性。

##	处理方法的数据绑定


![20170724175611235.jpg](https://i.loli.net/2018/10/10/5bbdadcccd2cf.jpg)

###	ConversionService

ConversionService 是 Spring 类型转换体系的核心接口，而 ConversionServiceFactoryBean 创建 ConversionService 内建了很多转换器，可完成大多数 Java 类型的转换工作。可通过 ConversionServiceFactoryBean 的 converters 属性注册自定义的类型转换器。

Spring 定义了三种类型的转换器接口：

*	Converter<S, T>
*	GenericConverter
*	ConverterFactory

ConversionServiceFactoryBean 的 converters 属性可接受  Converter、ConverterFactory、GenericConverter 或 ConditionalGenericConverter 接口的实现类，并把这些转换器的转换逻辑统一封装到一个 ConversionService 实例对象中。

``举个例子``，假设处理方法中有一个 User 类型的入参，希望讲一个格式化的请求参数字符串直接转换为 User 对象，该字符串的格式为：

>		<userName>:<password>:<realName>

我们可以定义一个将格式化的 String 转换为 User 对象的自定义转换器。

```Java
	public class StringToUserConverter implements Converter<String, User> {
		public User Converter(String source) {
			User user = new User();
			if (source != null) {
				String[] items = source.split(":");
				user.setUserName(items[0]);
				user.setPassword(items[1]);
				user.setRealName(items[2]);
			}
			return user;
		}
	}
```

接下来将它安装到 Spring 上下文中。

```XML
	...
	<mvc:annotation-driven conversion-service="conversionService" />
	<bean id="conversionService"
			class="org.springframework.context.support.ConversionServiceFactoryBean">
			<property name="converters">
					<list>
							<bean class="com.smart.domain.StringToUserConverter" />
					</list>
			</property>
	</bean>
```

然后发送的 URL 请求可以使用 `user=tom:1234:tomson` 的参数，最后会绑定到 Controller 方法的入参中。

### 数据格式化

Spring 引入的格式化框架中有一个重要的接口 `Formatter<T>`

```java
	package org.springframework.format;
	public interface Formatter<T> extends Printer<T>, Parse<T> {
		...
	}
```
Printer<T> 负责对象的格式化输出，Parser<T> 负责格式化对象的输入工作。Spring 提供了三个用于数字化对象格式化的实现类。

实现类|作用|
---|---|
NumberFormatter|数字类型对象的格式化|
CurrencyFormatter|货币类型对象的格式化|
PercentFormatter|百分数数字类型对象的格式化|

随着 Java 技术的发展，这种硬编码的格式化方式已经过时，可在 Bean 属性设置、Spring MVC 处理方法入参数据绑定、模型数据输出时自动通过注解应用格式化功能。


实际上，Spring 是基于对象转换框架植入“格式化”功能的。Spring 定义了一个实现 ConversionService 接口的 FormattingConversionService 实现类，该实现类扩展了 GenericConversionService,因此它既具有类型转换功能，又具有格式化功能。

FormattingConversionService 有一个对应的 FormattingConversionServiceFactoryBean 工厂类，可以在 Spring 上下文中后再一个 FormattingConversionService，通过这个工厂类，既可以注册自定义的转换器，还可以注册自定义的注解驱动逻辑。

使用前要先在配置文件中装配。

```XML
<mvc:annotation-driven conversion-service="conversionService" />
<bean id="conversionService"
		class="org.springframework.formatt.support.FormattingConversionServiceFactoryBean">
		<property name="converters">
				<list>
						<bean class="com.smart.domain.StringToUserConverter" />
				</list>
		</property>
</bean>
```

在 PO 中这样使用：

```Java
	public class User {
		...
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date birthday;

		@NumberFormat(pattern="#.###.##")
		private long salary;
	}
```

###	数据校验

应用程序在执行业务逻辑前，必须通过数据校验保证接受到的输入数据是正确合法的。

Spring MVC 中定义的 LocalValidatorFactoryBean 既实现了 Spring 的 Validator 接口，又实现了 JSR-303 的 Validator 接口。<mvc:annotation-driven/> 会默认装配一个 LocalValidatorFactoryBean，通过在处理方法的入参上标注 @Valid 注解，即可让 Spring MVC 在完成数据绑定后执行数据校验工作。

```Java
public class User{
		private String userId;

		@Pattern(regexp="w{4, 30}")
		private String userName;

		@Past	// 时间值必须是一个过去的时间
		@DateTimeFormat(pattern="yyyy-MM-dd")
		private Date birthday;

}
```

```Java
	public class UserController {
			@RequestMapping(path = "/handle")
			public String handle(@Valid @ModelAttribute("user")User user,
													BindingResult bindingResult) {
						if(bindingResult.hasErrors()) {
							return "/user/register";
						} else {
							return "/user/showUser";
						}
			}

	}
```



参考文章：

[SpringMVC中为什么要配置Listener和Servlet](https://blog.csdn.net/fcs_learner/article/details/74085053)

[SpringMVC启动过程详解](https://www.jianshu.com/p/843576c42ba1)

[【SpringMVC】数据处理-数据绑定流程分析](https://blog.csdn.net/u013040472/article/details/76033914)
