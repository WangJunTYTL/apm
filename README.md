APM
=============
__APM：__ Application Performance Monitor，是一款偏向于解决业务上的行为监控系统，可以帮助业务开发者快速构建自己的metric，集性能数据采集、展示、预警一套完整的业务监控解决方案，可以运用到任何语言的项目当中。

在业务层面，运维人员其实是很难介入到实际的业务编码中的，对于业务中存在的关键点，也许还只有业务开发者自己清楚所在，APM目的是帮助业务开发者快速构建自己的metric，对关键性的业务逻辑点加入监控行为。

![alt text](./reference/images/apm_heart.jpg)

提供以下功能特性：

- 实时数据采集：由Perf4j提供近实时的数据采集，与日志组件集成,如log4j、logback
- 数据分析引擎：可以自定义时间性能数据存储方案，默认提供MySql和ElasticSearch数据存储设计，可扩展，如常用的influxdb
- 数据图表：推荐Grafana，提供丰富的数据图表展示功能，可以支持常见的图表类型，可以支持让业务开发者自定义图表或者Dashboard
- 预警：灵活的预警规则定义，支持通过表达式语言定义预警规则和消息模板

### 快速上手
- 下载项目：git clone https://github.com/WangJunTYTL/apm.git
- 构建：sh  build.sh （如果平台不可以执行shell脚本，一定要按照脚本执行步骤进行手动构建）
- 启动： apm-dashboard
    - 配置数据源，进入到apm-dashboard项目，在conf.properties中设置mysql数据源，请注意，链接的数据库需要拥有执行建表语句的权限。
    - 启动项目， mvn jetty:run （初次启动，建议直接通mvn jetty:run的方式启动）
    - 在浏览器中访问：127.0.0.1:8888 ，项目本身集成监控，可以在这里查看你刚才访问过的http地址和当前服务的jvm基本信息。

### 项目文档

1. [Java项目中数据采集方式](./reference/apm_gather.md)
2. [怎样将数据导入到MySql](./reference/apm_mysql.md)
3. [怎样将数据导入到ElasticSearch](./reference/apm_elasticsearch.md)
4. [基于MySql数据源的展示与预警平台](./reference/apm_dashboard.md)
5. [基于Elastic和Grafana的展示平台](./reference/apm_other.md)



### 交流

QQ群：365133362 群名称：互联网从业者    
