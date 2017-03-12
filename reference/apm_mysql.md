将数据导入到MySql
------------
MySql是各个公司常用到的数据库，足够稳定和灵活，如果你打算短期存储数据，apm提供导入MySql的appender组件类，你可以加入下面依赖

```
<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>apm-perf4j</artifactId>
    <version>2.6-SNAPSHOT</version>
</dependency>
```
然后在日志文件配置以下信息

```
<appender name="JdbcAppender" class="com.peaceful.apm.perf4j.appender.JdbcLogbackAppender">
    <param name="url"
           value="${datasource.url}"></param>
    <param name="user" value="${datasource.user}"></param>
    <param name="password" value="${datasource.password}"></param>
    <param name="service" value="Apm"></param>
</appender>

```
除配置数据库的连接地址、用户名、密码外，你还需要配置service名，然参数的意义是，用于各服务之间进行分表,例如上述服务将自动创建下面表

> APM_SERVICE：用于注册服务

> APM_STATISTICS_Apm：服务对应的性能数据收集
  
另外apm-dashboard支持的数据源也是MySql,后文会介绍该项目的意义  

