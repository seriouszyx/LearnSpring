#   Chapter 13

Spring JDBC 是 Spring 所提供的持久层技术。

与 ORM 框架相比，Spring JDBC 更灵活。如在完全依赖查询模型动态产生查询语句的综合查询系统中，JDBC 是惟一的选择。

Spring JDBC 使用 update() 方法更改数据，使用 PreparedStatement 绑定参数时，参数索引从 1 开始，而非从 0 开始。当需要批量更改大量数据时，可以使用 batchUpdate() 方法。

Spring JDBC 有两种主要的查询方法。RowMapper 看作采用批量化数据处理策略，RowCallBackHandler 采用流化处理策略。查询单值数据时，可以使用 queryForObject() 等方法。

对于存取大对象数据 LOB ，Spring JDBC 提供了一些接口，不过一般不宜将他们存储带数据库中，而应存储到文件服务器中。

