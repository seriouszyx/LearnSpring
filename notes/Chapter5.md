#   第5章 在 IoC 容器中装配 Bean

本章详细地装配 Bean 的各种方法。

基于 XML 的配置方式比较基础，现在一般采用基于 schema 的配置格式。根节点中通过 xmlns 对文档所引用的`命名空间`进行声明。

##  依赖注入

而依赖注入的方式一般使用属性注入，在配置文件中，每个属性对应一个`<property>`标签，name 为属性的名称，容器启动时，Spring 只会检查 Bean 是否有对应的 Setter 方法，至于 Bean 中是否有对用的属性成员则不做要求。

构造函数注入的方式保证一些必要的属性在 Bean 实例化时就得到设置。分为按`类型匹配入参`、`按索引匹配入参`、`联合使用类型和索引匹配入参`三种。

构造函数注入方法要求 <constructor-arg> 的元素中有一个 type 属性，它为 Spring 提供了判断配置项和构造函数入参的对应关系。此外，Spring 的配置文件采用和元素标签顺序无关的策略，如果参数未指定 index，<constructor-arg> 的位置不会对最终配置效果产生影响。

按索引入参方法即不配置 type 属性，而是增加 index 属性，可以消除多个相同属性匹配的不确定性。

而联合使用类型和索引匹配入参方法即同时注明 type 和 index 属性，准确性较高。

不过，两个 bean 同时调用彼此作为参数注入的话就会产生循环依赖问题，Spring 容器将无法重新启动。用户只需将构造函数注入方法调整为属性注入就可以了。

还有一种工厂注入的方法，创建自己的工程类，实现非静态或静态的方法，不过一般不去使用这种方法。

##  注入参数

一些基本数据类型及封装类、String 等类型都可以采用字面值注入的方法。

为了防止字面值中存在一些特殊字符（&、<、>、"、'），XML 提供了两种解决方法，一种是将字面值写入 `<![CDATA[]]]>` 中，以一种是使用 XML 转义字符表示这些特殊字符。

同样可以引入其他 Bean 来注入。<ref> 元素可以通过2个属性引用容器中的其他 Bean（Spring 4.0 删除了 local 属性的支持）。

*   bean：通过改属性可以引用同一容器或父容器中的 Bean（最常见的形式）
*   parent：引用父容器中的 Bean

假设 car Bean 只被 boss Bean 引用，而不被容器中任何其他 Bean 引用，则可以将 car 以内部 Bean 的方式注入 Boss 中。

另外，如果想为属性赋空值，可以使用 `<null/>`。

为集合类型属性赋值都提供了专属的配置标签，而且 Spring 支持集合合并的功能，可以使用 `merge="true"` 指示子 bean 和父 bean 中的同名属性值进行合并。

`使用 p 命名空间`和`采用属性而非子元素配置信息`的方式可以极大地简化配置方式。

最后，Spring 为厌恶配置的开发人员提供了一种轻松的方法，可以按照某些规则进行 Bean 的自动装配。`<bean>` 元素提供了一个指定自动装配类型的属性：`autowire="<自动装配类型>"`。然而，XML 配置方式很少启用自动装配功能，而基于注解的配置方式则默认采用 byType 的自动装配策略。

##  Bean 作用域

Spring 4.0 支持5种作用域类型：

*   singleton
*   prototype
*   request
*   session
*   globalSession

一般情况下，无状态或者状态不可变的类适合使用单例模式。传统开发中，由于 DAO 类持有 Connection 这个非线程安全变量，所以未采用单例模式。不过 Spring 利用 AOP 和 LocalThread 功能，对非线程安全的变量（或称状态）进行了特殊处理，使这些非线程安全的类变成了线程安全的类。

因此，scope 属性的默认作用域为 singleton。

在默认情况下，Spring 的 ApplicationContext 在启动时自动实例化所有 singleton 的 Bean 并缓存到容器中，可以用过 lazy-init 属性进行控制。

prototype 作用域与 singleton 不同，每次调用 getBean() 都会返回一个新的实例，而且将 Bean 交给调用者后，就不在管理他的生命周期。

其他三个作用域都是与 web 应用环境相关的，使用前需要配置 RequestContextListener，不过这三种作用域在实际开发中用的不多。

##  基于注解的配置

书中详细地讲述了基于 XML 配置 Bean，然而最常用的还是基于注解配置的方法。

在类声明处标注 `@Component`，它就可以被 Spring 识别，Spring 容器自动将 POJO 转换为容器管理的 Bean。

除了 @Component，Spring 还提供了三个功能基本相同的注解，分别是 `@Repository`、`@Service` 和 `@Controller`。

在 XML 文件中，可以定义扫描包：

```xml
<context:conponent-scan base-package="com.smart" resource-pattern="com.smart.anno">
```

同时，可以使用 `@Autowired` 强大的自动注入，一般在私有变量的 setter  方法上标注，还可以用 `@Qualifier` 指定注入 Bean 的名称；`@Lazy` 实施延迟依赖注入，即“懒加载”；用 `@Scope` 指定作用域。

##  其他装配方式

在类名上标注 `@Configuration` 即可将 POJO 类变成 Bean 配置信息，这是使用 Java 类提供 Bean 定义信息的方式。

Spring 4.0 支持使用 Groovy DSL 提供 Bean 的定义信息，并提供了 GenericGroovyApplicationContext 类启动 Spring 容器。

另外，Spring 也支持通过编码方式动态添加 Bean。

不过，项目中通常采用“基于 XML+基于注解”的配置方式，所以其他方式仅作为了解。








