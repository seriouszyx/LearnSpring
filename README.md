#   Spring 框架学习

结合书籍[《精通Spring4.x企业应用开发实战》](https://book.douban.com/subject/26952826/)学习 Spring 框架，并记录每章节的重点内容。

本书的配套源码记录在 [source code](https://github.com/seriouszyx/LearnSpring/tree/master/source%20code/code) 中，而 mycode 则是平时学习过程中做的一些练习，可能是简单实现框架的某个核心模块，也可能是为了熟悉框架而写的 demo。

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

开始进入 AOP 的学习，开始上课了，满课状态下不继续学习框架。

*   Day5 

*   Day6 

*   Day7 

*   Day8

*   Day9 

*   Day10 

###    完成情况


? 第1篇 基础篇


- [x] 第1章 Spring概述	

- [x] 第2章 快速入门	

- [ ] 第3章 Spring Boot	

? 第2篇 核心篇

- [x] 第4章 IoC容器	

- [x] 第5章 在IoC容器中装配Bean

- [x] 第6章 Spring容器高级主题	

- [ ] 第7章 Spring AOP基础

- [ ] 第8章 基于@AspectJ和Schema的
AOP

* [ ] 第9章 Spring SpEL

? 第3篇 数据篇

- [ ] 第10章 Spring对DAO的支持

- [ ] 第11章 Spring的事务管理
