数据展示
--------------

## apm-dashboard
perf4j是支持图表显示的，可是并不太好用。第一，它的图表渲染是通过调用google的chartApi进行渲染，在如今的中国google还是无法访问的。第二它的图表只可以显示实时的数据、如果
想查看指定时间点的数据是不支持，而且它只有配置的tag才可以查看，这个图表功能基本上是无法使用到生产环境的。所以项目为了解决这个问题，附带了一个简单的dashboard。

如果你想使用该项目，你需要加入构建apm-perf4j，它是修改了perf4j的部分源码修改的地方

* 图表渲染的功能改成了百度的Echart
* 通过servlet暴漏数据的的api改成了可以支持实时数据查询也支持指定时间点数据查询
* 数据必须通过JdbcLog4jAppender保存到数据库中才可以支持指定时间点数据

注意：如果你已经使用perf4j，修改的版本不会对你当前的使用有影响

## 如果你想使用附带的dashboard，你需要加入下面配置

升级perf4j版本

```
 <dependency>
     <groupId>org.perf4j</groupId>
     <artifactId>perf4j</artifactId>
     <!--构建的apm-perf4j的版本-->
     <version>x.x-version</version>
 </dependency>
```

加入下面模块的依赖
```
<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>apm-aggregate</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>apm-plugin</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

这里以log4j为例，配置log4j.xml

```
<!--数据存储位置，这里是存储到项目的目录下的apm.db文件中-->
<appender name="Perf4jSQLiteJdbcAppender"
          class="com.peaceful.apm.aggregate.JdbcLog4jAppender">
    <param name="url" value="jdbc:sqlite:apm.db"></param>
    <param name="driver" value="org.sqlite.JDBC"></param>
</appender>
<!--在聚合appender下加入上述appender-->
<appender name="CoalescingStatistics" class="org.perf4j.log4j.AsyncCoalescingStatisticsAppender">
    <param name="TimeSlice" value="30000"/>
    <param name="createRollupStatistics" value="false"></param>
    <appender-ref ref="Perf4jSQLiteJdbcAppender"></appender-ref>
</appender>
```

在你的web项目中配置的web.xml文件加入下面servlet
```
<servlet>
    <servlet-name>perf4j</servlet-name>
    <servlet-class>org.perf4j.servlet.APMGraphingServlet</servlet-class>
</servlet>
<servlet-mapping>
    <servlet-name>perf4j</servlet-name>
    <url-pattern>/apm</url-pattern>
</servlet-mapping>
```

执行下面命令启动apm-dashboard项目

```
mvn jetty:run -f apm-dashboard/pom.xml
```
访问[http://127.0.0.1:8889](127.0.0.1:8889)

然后点击添加机器把刚才配置的api地址添加进去，所属集群与节点可以自定义、然后刷新点击刚刚添加的机器进入到实时显示页面下，如果没有图表生成，那是因为你还没有埋点或者埋点没有执行。
有关配置开发可以参考apm-demo项目







