##  Chapter 8

第八章首先介绍用 @AspectJ 的方法定义一个切面。

在 Java 5.0 引入了注解，完成特定功能。注解的基本原则是不能直接干扰程序代码的运行，第三方工具通过Java反射机制读取注解的信息，并根据这些信息更改目标程序的逻辑，这也正是 Spring AOP 对 @AspectJ 提供支持所采取的方法。

使用 @AspectJ 定义切面的一般流程为，将一个 POJO 类标注 @Aspect，这个类即被标记为切面。然后可以使用 AspectJProxyFactory 类来将目标对象和切面类对应起来，不过更常见的做法是，在配置文件中配置切面和目标对象，通过 ApplicationContext 类定位配置文件，并使用 getBean() 方法获得代理对象。

@AspectJ 的切点表达式函数有关键字和操作参数组成，如`execution(* greetTo(..))`中 `execution` 为关键字，`* greetTo(..)` 为操作参数。

Spring 支持9个 @AspectJ 表达式函数，其中最常用的是 `execution()`。




