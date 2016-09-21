Java项目中基本的数据采集方式
-------------

#### 首先需要在你的Java项目中加入perf4j的依赖
```
<dependency>
    <groupId>org.perf4j</groupId>
    <artifactId>perf4j</artifactId>
    <!--x.x是你使用的版本-->
    <version>x.x</version>
</dependency>
```
#### 向下面这样在你的代码块编写：
```
StopWatch watch = new SlfStopWatch()
Your code...
........
watch.stop("apm.test")
```
#### 上面代码块每执行一次都这将产生一条log数据， 格式如下：
```
start[1474272020799] time[866] tag[apm.test]
start[1474272020799] time[866] tag[apm.test]
start[1474272020799] time[866] tag[apm.test]
...
```
所以如果你的项目不是Java的项目，你也可以编写自己的数据采集方式，不必使用提供的StopWatch计时器类，只要可以产出这样格式的日志数据,先记住基本的日志数据格式，
后面会介绍在非Java项目中的使用方式，这点会很关键。

#### 根据当前系统使用的日志组件配置日志文件

这里以log4j为例，在log4j.xml配置文件加入下面配置信息，性能数据会输出到控制台

```
    <appender name="console" class="org.apache.log4j.ConsoleAppender">
        <param name="Target" value="System.out"/>
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern" value="%d{yy-MM-dd HH:mm:ss,SSS} %-5p %c(%F:%L) ## %m%n"/>
        </layout>
    </appender>

    <!--handle msg appender-->
    <appender name="CoalescingStatistics" class="org.perf4j.log4j.AsyncCoalescingStatisticsAppender">
        <param name="TimeSlice" value="60000"/>
        <appender-ref ref="console"/>
    </appender>
    <!--org.perf4j.TimingLogger-->
    <logger name="org.perf4j.TimingLogger" additivity="false">
        <level value="INFO"/>
        <appender-ref ref="CoalescingStatistics"/>
    </logger>
```
- 面向appender进行开发：handle msg appender
    > perf4j有个很好的设计地方，就是它采用和日志组件集成的方式，来编写自己的数据处理模块和结果输出模块，如果你对日志组件很了解
    ，如log4j、logback，很清楚appender的概念，那你会很容易理解下面的配置用意（这里还是简单的描述下：appender主要是用来表示数据的输出地：比如常用console、file，另外appender在日志组件中也支持继承，也就是说appender还可以将消息向下面的appender进行传递）。
    
    > **TimeSlice：** 数据在本地进行初次聚合，聚合周期为TimeSlice值，建议值60000，即一分钟。聚合后的数据输出到console
    
    ```
    Performance Statistics   2016-09-19 16:12:00 - 2016-09-19 16:13:00
    Tag                                                  Avg(ms)         Min         Max     Std Dev       Count
    http.dashboard                                         290.5          71         944       377.3           4
    http.dashboard.node.list                                27.0           3          98        41.0           4
    http.request.total                                     158.8           3         944       299.0           8
    ```    
- 为什么日志命名空间是：org.perf4j.TimingLogger

    >SlfStopWatch在调用stop方法时默认产生的日志命名空间是这样的，你也可以自己设置
    
数据采集的功能由perf4j提供，想了解更多关于信息采集的方式，可到网上搜索资料科普

