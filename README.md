APM
=============
__APM：__ Application Performance Monitor，偏向于解决业务监控，帮助业务开发者快速构建自己的metric，用于服务性能数据收集、可视化、预警，可以在任何语言的项目当中使用。

在线上业务运维层面，运维人员其实是很难介入到实际的业务编码中，对于业务中存在的关键点，也许只有业务开发者自己清楚关键点所在，APM目的是让业务开发者快速构建自己的metric，对关键性的业务逻辑点加入监控行为。

![alt text](./reference/images/apm_heart.jpg)


## 使用方式：

````
StopWatch stopWatch = APM.createStopWatch();
{
    stopWatch.start("tag);
    do something......
    stopWatch.end;
}
````

当上面代码被执行时会计算出如下指标
````
(time=1517217857279, tag=tag, count=4329, mean=2.0, min=0.0, t50=2.0, t75=3.0, t95=5.0, t99=5.0, t999=7.0, max=7.0)
````

这仅是APM采集数据的方式，APM基于采集的数据可以做很多的事情

提供以下功能特性：

#### 监控指标
- 性能监控：对一处代码块、一个方法或一次服务调用的行为进行记录分析: TPS、Mean、Max、Min、Std指标，由Perf4j支持这些指标的计算
- JVM监控: 通过JMX定时采集数据：HeapMemory、NonHeapMemory、GCTime、GCCount、ThreadCount


#### 数据聚合
- 按照service、hostname、metric聚合：在每个业务服务器采集完原始数据后，按照每分钟聚合一次，然后上传到数据存储服务器
- 然后数据上传到存储服务器上，如ES，支持通过按照service、metric对整个完成服务的数据实时聚合

#### 数据存储

- 支持log、ES、MySql、Graphite及自定义数据保存方式
- 与日志组件集成,如log4j、logback，可通过LogAppender的方式定义数据输出

#### 数据可视化
- 数据分析引擎：可以自定义时间性能数据存储方案，默认提供MySql和ElasticSearch数据存储设计，可扩展，如常用的influxdb
- 数据图表：推荐Grafana，提供丰富的数据图表展示功能，可以支持常见的图表类型，可以支持让业务开发者自定义图表或者Dashboard
- 预警：灵活的预警规则定义，支持通过表达式语言定义预警规则和消息模板

### 快速上手
- 下载项目：_git clone https://github.com/WangJunTYTL/apm.git_
- 本地测试：执行 _sh build.sh_ 启动后访问127.0.0.1:8888，该Web系统只是一个简易的Dashboard，用于展示数据和预警配置（如果平台不可以执行shell脚本，一定要按照脚本执行步骤进行手动构建)

### 项目文档

1. [Java项目中数据采集方式](./reference/apm_gather.md)
2. [怎样将数据导入到MySql](./reference/apm_mysql.md)
3. [怎样将数据导入到ElasticSearch](./reference/apm_elasticsearch.md)
4. [基于MySql数据源的展示与预警平台](./reference/apm_dashboard.md)
5. [基于Elastic和Grafana的展示平台](./reference/apm_other.md)


### 交流

QQ群：365133362 群名称：互联网从业者    
