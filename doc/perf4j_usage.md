# 性能数据采集配置

### 在你的项目中加入以下依赖

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

### 配置log4j

**注意：** 如果你正在使用logback并成功配置了logback的使用，请你增加logback的配置文档，多谢！

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

### 配置api

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

### api说明

如果是要对接到公司自己的监控系统，可以掌握下面几个api，以上面配置的地址是/perf4j为例

1. 实时数据：/perf4j?method=now
2. 历史数据：/perf4j?method=history&tag=XX&from=timestamp&to=timestamp from默认值是六个小时前，to默认是当前
3. jvm数据：/perf4j?method=history&tag=XX&from=timestamp&to=timestamp from默认值是六个小时前，to默认是当前

响应数据均为Json格式

### 最简单的埋点方式
```
StopWatch watch = new SlfStopWatch();
watch.start("tag");
....业务逻辑执行
watch.stop();
```
perf4j还提供了丰富的埋点方式，这里不作具体介绍

