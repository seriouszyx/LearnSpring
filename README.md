#   Spring 框架学习

结合书籍[《精通Spring4.x企业应用开发实战》](https://book.douban.com/subject/26952826/)学习 Spring 框架，并记录每章节的重点内容。

本书的配套源码记录在 [source code](https://github.com/seriouszyx/LearnSpring/tree/master/source%20code/code) 中，而 [mycode](https://github.com/seriouszyx/LearnSpring/tree/master/mycode) 则是平时学习过程中做的一些练习，可能是简单实现框架的某个核心模块，也可能是为了熟悉框架而写的 demo。

## 学习历程

为了更高的效率，我希望给予自己一些压力，在10天内完成该书的阅读并初步掌握 Spring 框架的使用及简单原理。下面是时间以及任务完成情况，每天都会进行更新。

### 记录

*   Day1

1、2两章简单介绍了 Spring 的历史、Spring 4.x 的新特性以及搭建一个论坛的登录模块，让读者快速了解使用 Spring 工作的流程。

而第4章也是较为重要的一章，介绍了 Spring 最核心的 IoC 容器和`控制反转`的概念，通过对反射、XML解析等基础知识的学习，实现了简单的 [IoC容器](https://github.com/seriouszyx/LearnSpring/tree/master/mycode/SimpleIoC)，并总结实现过程写了一篇文章[“Spring IoC容器浅析及简单实现”](https://blog.csdn.net/qq_40950957/article/details/82811507)。

简单了解 BeanFactory、ApplicationFactory 和 WebApplicationFactory 三个 Spring 框架的核心接口，逐渐加深对 Spring 的原理理解和注入方法。

然而对生命周期和 Resource 的理解并不深刻。

>   笔记：[第四章笔记](https://github.com/seriouszyx/LearnSpring/blob/master/notes/Chapter4.md)



*   Day2

进入第5章学习，第5章详细地讲述在 IoC 容器中装配 Bean 的四个方法，包括一些原理的分析。

根据我看过的一些源码还有本书笔者的建议，一般采用 XML 配置 DataSource、SessionFactory 等资源 Bean，在 XML 中利用 aop、context 命名空间进行相关主题的配置。其他所有项目中开发的 Bean 都通过基于注解配置的方法进行配置，即整个项目采用“基于 XML+基于注解”的配置方式，很少采用基于 Java 类的配置方法。

不过基于 groovy 的配置方式可以处理更复杂的逻辑，而且 Spring4.0 全面支持 groovy，前景一片美好，以后应该会多去接触它。

>   笔记：[第五章笔记](https://github.com/seriouszyx/LearnSpring/blob/master/notes/Chapter5.md)

*   Day3

完成了第6章的学习，讲述了 IoC 的底层实现。

*   Day4

开始进入 AOP 的学习，读到一篇很不错的讲动态代理的文章——[《10分钟看懂动态代理设计模式》](https://juejin.im/post/5a99048a6fb9a028d5668e62)，标题党石锤，我看了超过100分钟都不太懂。

动态代理是 AOP 底层实现的基础，还是尽量理解。

开始上课了，满课状态下不继续学习框架。

*   Day5

今天在看 AOP 的时候很难受，跑过几个开源的基于 SSM 的项目，发现很少用到书中所写的方法配置，Spring 已经进行了更好的封装，如果单纯想使用 Spring 的话，没有必要详细地看这部分内容。

第九章跳过，目的是先抓住框架的主线进行快速学习，然后再补充相关的知识要点。

第2篇核心篇的重中之重就是 IoC 和 AOP 以及它们的多种配置方式，这里详细讲述了框架的思想。

进入第3篇数据篇，仍不算是真正开始了应用方面的讲解，而是在第2篇的基础上，介绍了 Spring 事务管理和对 DAO 的支持，由于操作系统方面的知识薄弱，在看 ThreadLocal 时云里雾里。

希望能用一天的时间快速结束第3章，开始应用篇的学习。

*   Day6

灵活的 Spring JDBC 和方便的 ORM 框架。因为之前接触过 mybatis，并且 SSM 框架目前使用量日益增加，所以并没有详细地阅读 hibernate 这一部分。

然而本书后面的实例好像是用 hibernate 做持久层框架的，到时候可能会用 mybatis 重写一下。

>   笔记：[第十三章笔记](https://github.com/seriouszyx/LearnSpring/blob/master/notes/Chapter13.md)

*   Day7

彻底告别`数据篇`，进入激动人心的应用篇。

上来就跳过两章，直接进入第十七章——Spring MVC 的学习。看过刚开始介绍的 demo，感觉开发简直不能再简便了，不过给的实例代码跑不起来，大概要自己手写一次。

>   笔记：[第十七章笔记](https://github.com/seriouszyx/LearnSpring/blob/master/notes/Chapter17.md)


*   Day8

重新实现了第二节的 [demo](https://github.com/seriouszyx/LearnSpring/tree/master/mycode/MVC-Demo), Spring MVC 可以看作独立的一个框架，非常重要，通过 demo 的学习也可以看出来 Spring MVC 将服务端与客户端之间数据的传输变得异常简单。

Spring MVC 的内容大概看了一半左右，一边看一边整理笔记，有不懂得地方马上去 Google 一下，毕竟这么重要的部分真的不能有一点理解上的妥协。

计划明天结束 Spring MVC,后天完成一个小项目结束这个 Repository。


*   Day9

*   Day10

###    完成情况


- 第1篇 基础篇


- [x] 第1章 Spring概述

- [x] 第2章 快速入门

- [ ] 第3章 Spring Boot

- 第2篇 核心篇

- [x] 第4章 IoC容器

- [x] 第5章 在IoC容器中装配Bean

- [x] 第6章 Spring容器高级主题

- [x] 第7章 Spring AOP基础

- [x] 第8章 基于@AspectJ和Schema的AOP

- [ ] 第9章 Spring SpEL

- 第3篇 数据篇

- [x] 第10章 Spring对DAO的支持

- [x] 第11章 Spring的事务管理

- [x] 第13章 使用 Spring JDBC 访问数据库

- [x] 第14章 整合其他 ORM 框架

- 第4篇 应用篇

- [ ] 第15章 Spring Cache

- [ ] 第16章 任务调度和异步执行器

- [ ] 第17章 Spring MVC

- [ ] 第18章 实战案例开发

- 第5篇 提高篇

- [ ] 第19章 Spring OXM

- [ ] 第20章 实战型单元测试
