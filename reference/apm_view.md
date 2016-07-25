数据展示
--------------

## apm-dashboard
perf4j是支持图表显示的，可是比较简陋也不太好用。第一，它的图表渲染是通过调用google的chartApi进行渲染，在如今的中国google还是无法访问的。第二它的图表只可以显示实时的数据、如果
想查看指定时间点的数据是不支持，而且它只有配置的tag才可以查看，这些原因导致它基本上无法使用到生产环境中。针对这些问题，apm-dashboard项目是一个专门用于perf4j的监控图表展示项目。

如果你想使用该项目，你需要先了解下面几点,它对perf4j的源码做了写改动

* perf4j的图表渲染的功能改成了百度的Echart
* 需要通过servlet暴漏数据
* 数据必须通过JdbcLog4jAppender保存到数据库中才可以支持查看指定时间点数据

注意：如果你已经使用perf4j，这些修改不会对你当前的使用有影响

## 如果你想使用附带的dashboard，你需要加入下面配置

配置perf4j

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
<!--提供多种appender,可以把本地聚合后的数据输入到console、file、db等等-->
<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>apm-aggregate</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<!--提供常用的组件监控插件,如jvm、api监控-->
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
上面配置可以让你查看指定时间点的监控数据,如果你想查看实时固定的业务监控数据,你可以加入下面配置

```
<!--实时图表配置,每个appender将会渲染出一张图表-->
<appender name="ExecutionTimes"
          class="com.peaceful.apm.aggregate.Log4jGraphingStatisticsAppender">
    <!-- Possible GraphTypes are Mean, Min, Max, StdDev, Count and TPS -->
    <param name="GraphType" value="Mean"/>
    <!-- The tags of the timed execution blocks to graph are specified here -->
    <!--<param name="TagNamesToGraph" value="http.request,order-message"/>-->
</appender>

<appender name="ExecutionCount"
          class="com.peaceful.apm.aggregate.Log4jGraphingStatisticsAppender">
    <param name="GraphType" value="Count"/>
</appender>

<appender name="ExecutionStdDev"
          class="com.peaceful.apm.aggregate.Log4jGraphingStatisticsAppender">
    <param name="GraphType" value="StdDev"/>
</appender>

<appender name="ExecutionTPS"
          class="com.peaceful.apm.aggregate.Log4jGraphingStatisticsAppender">
    <param name="GraphType" value="TPS"/>
    </appender>
```

在你的web项目中配置的web.xml文件加入下面servlet,用于通过api的方式获取监控数据

```
<servlet>
    <servlet-name>perf4j</servlet-name>
    <servlet-class>com.peaceful.apm.aggregate.servlet.Log4jBaiduEchartGraphingServlet</servlet-class>
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
有关配置开发可以参考apm-demo项目的log4j.xml和web.xml配置,如果你使用的日志组件是logback,可以吧上述配置中出现Log4j的地方换成Logback即可


## 对接到第三方监控

这里以grafana为例,grafana是一个性能监控项目的展示项目,它可以支持好几种时间序列的数据库:如graphite、elasticsearch、influxDB等等,
grafana地址:http://grafana.org,我们像下面把数据写入到elasticsearch中

```
<!--基于elastic search的1.x版本实现-->
 <appender name="Perf4jESAppender"
           class="com.peaceful.apm.aggregate.ElasticLog4jAppender">
     <param name="url" value="127.0.0.1"></param>
     <param name="port" value="9300"></param>
     <param name="indexPrefix" value="apm-"></param>
     <param name="clusterName" value="elasticsearch"></param>
 </appender>
```

然后我们在grafana配置我们的elasticsearch数据源,按照grafana的说明配置图表即可








