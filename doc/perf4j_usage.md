# 性能数据采集

如果你还没有使用过perf4j进行性能监控，可以参照这篇博客：[http://www.infoq.com/cn/articles/perf4j/](http://www.infoq.com/cn/articles/perf4j/)。这里只简单介绍下perf4j的配置

在你的项目中加入以下依赖

```
 <dependency>
      <groupId>org.perf4j</groupId>
      <artifactId>perf4j</artifactId>
      <version>2.0-SNAPSHOT</version>
 </dependency>

 <dependency>
      <groupId>org.xerial</groupId>
      <artifactId>sqlite-jdbc</artifactId>
      <version>3.8.11.2</version>
 </dependency>

```

如果你使用的log4j作为日志组件实现，可以在log4j.xml加入如下配置,可以参照demo项目中log4j的配置

```
    <!--实时图表配置,每个appender将会渲染出一张图表-->
    <appender name="ExecutionTimes"
              class="org.perf4j.log4j.GraphingStatisticsAppender">
        <!-- Possible GraphTypes are Mean, Min, Max, StdDev, Count and TPS -->
        <param name="GraphType" value="Mean"/>
        <!-- The tags of the timed execution blocks to graph are specified here -->
        <!--<param name="TagNamesToGraph"-->
        <appender-ref ref="graphsFileAppender"/>
    </appender>

    <appender name="ExecutionCount"
              class="org.perf4j.log4j.GraphingStatisticsAppender">
        <!-- Possible GraphTypes are Mean, Min, Max, StdDev, Count and TPS -->
        <param name="GraphType" value="Count"/>
        <!-- The tags of the timed execution blocks to graph are specified here -->
        <param name="TagNamesToGraph" value="queue_pop,queue_push,http_request,order-message"/>
        <appender-ref ref="graphsFileAppender"/>
    </appender>

    <appender name="ExecutionStdDev"
              class="org.perf4j.log4j.GraphingStatisticsAppender">
        <!-- Possible GraphTypes are Mean, Min, Max, StdDev, Count and TPS -->
        <param name="GraphType" value="StdDev"/>
        <!-- The tags of the timed execution blocks to graph are specified here -->
        <param name="TagNamesToGraph" value="http_request"/>
        <appender-ref ref="graphsFileAppender"/>
    </appender>

    <appender name="ExecutionTPS"
              class="org.perf4j.log4j.GraphingStatisticsAppender">
        <param name="GraphType" value="TPS"/>
        <param name="TagNamesToGraph" value="http_request,queue_pop,queue_push,/user/level/auth"/>
        <appender-ref ref="graphsFileAppender"/>
    </appender>

    <!--历史数据存放目录-->
    <appender name="HistoryData"
              class="org.perf4j.log4j.SqlLiteAppender">
        <!--数据保留路径-->
        <param name="path" value="/data/logs/demo/perf4j.db"></param>
        <!--数据保留天数,默认是30天-->
        <param name="storage" value="25"></param>
    </appender>

    <appender name="CoalescingStatistics" class="org.perf4j.log4j.AsyncCoalescingStatisticsAppender">
        <param name="TimeSlice" value="30000"/>
        <param name="createRollupStatistics" value="true"></param>
        <appender-ref ref="console"/>
        <appender-ref ref="ExecutionTPS"></appender-ref>
        <appender-ref ref="ExecutionTimes"></appender-ref>
        <appender-ref ref="ExecutionCount"></appender-ref>
        <appender-ref ref="ExecutionStdDev"></appender-ref>
        <appender-ref ref="HistoryData"></appender-ref>
    </appender>

    <appender name="graphsFileAppender" class="org.apache.log4j.FileAppender">
        <param name="Path" value="/data/logs/demo/graphFile.log"/>
    </appender>

    <!--log config start-->
    <logger name="org.perf4j.TimingLogger" additivity="false">
        <level value="INFO"/>
        <appender-ref ref="CoalescingStatistics"/>
    </logger>
    <!-- per4j-appender-end -->
```

如果需要在dashboard项目中观察性能图表，需要配置Web地址

```
  <servlet>
        <servlet-name>perf4j</servlet-name>
        <servlet-class>org.perf4j.servlet.VitaGraphingServlet</servlet-class>
  </servlet>
  <servlet-mapping>
      <servlet-name>perf4j</servlet-name>
      <url-pattern>/perf4j</url-pattern>
  </servlet-mapping>
```

最简单埋点方式
```
StopWatch watch = new SlfStopWatch();
watch.start("tag");
....业务逻辑执行
watch.stop();
```
perf4j还提供了更多的埋点方式，可以参考网上博客，这里不作具体介绍

