数据采集
-------------

数据采集功能由Perf4j提供，有关Perf4j的详细信息可到网上搜索，这里只介绍下perf4j简单的使用

## 加入perf4j的依赖

```
<dependency>
    <groupId>org.perf4j</groupId>
    <artifactId>perf4j</artifactId>
    <!--x.x是你使用的版本-->
    <version>x.x</version>
</dependency>
```
## 根据当前系统使用的日志组件配置日志文件

这里以log4j为例，在log4j.xml配置文件加入下面配置信息，性能数据会输出到控制台。有关perf4j与日志组件集成的更多信息，不作介绍,可以网上科普

```
    <!--log4j console appender-->
    <appender name="console" class="org.apache.log4j.ConsoleAppender">
        <param name="Target" value="System.out"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yy-MM-dd HH:mm:ss,SSS} %-5p %c(%F:%L) ## %m%n"/>
        </layout>
    </appender>

    <!--数据在本地进行初次聚合，聚合周期为TimeSlice值，建议值60000，即一分钟-->
    <appender name="CoalescingStatistics" class="org.perf4j.log4j.AsyncCoalescingStatisticsAppender">
        <!--本地聚合周期-->
        <param name="TimeSlice" value="60000"/>
        <!--在console中输出聚合后的数据-->
        <appender-ref ref="console"/>
    </appender>

    <!--perf4j是把采集到信息转化为日志消息输入到消息组件,然后由上面的AsyncCoalescingStatisticsAppender处理-->
    <logger name="org.perf4j.TimingLogger" additivity="false">
        <level value="INFO"/>
        <appender-ref ref="CoalescingStatistics"/>
    </logger>
```


## 埋点采集数据

```
StopWatch watch = new SlfStopWatch();
你的业务逻辑代码...
watch.stop("A")
```
这里只是介绍通过编码的方式去埋点，Perf4j也提供了基于AOP或基于注解的方式埋点

这就是有关数据采集的介绍，主要是关于perf4j的使用，由于网上有很多perf4j的使用文章，所以不作具体使用介绍

