APM
=============
__APM：__ Application Performance Monitor，是一款集性能数据采集、展示、预警一套完整的业务监控解决方案。

![alt text](./reference/images/apm_heart.jpg)

可以提供以下功能：

- 近实时的数据采集，依赖perf4j
- 自定义性能数据存储方案，比如mysql、influxdb、elasticserach
- 丰富的数据图表展示功能，依赖grafana
- 灵活的预警规则定义，支持表达式语言定义规则和消息模板

### 快速上手
- 下载项目：git clone https://github.com/WangJunTYTL/apm.git
- 构建：sh  build.sh （如果平台不可以执行shell脚本，一定要按照脚本执行步骤进行手动构建）
- 启动： apm-dashboard
    - 配置数据源，进入到apm-dashboard项目，在conf.properties中设置mysql数据源，请注意，链接的数据库需要拥有执行建表语句的权限。
    - 启动项目， mvn jetty:run （初次启动，建议直接通mvn jetty:run的方式启动）
    - 在浏览器中访问：127.0.0.1:8888 ，（你也许上来会先随便点击看下各个功能看下，这样更好）项目本身集成监控，可以在这里查看你刚才访问过的http地址和当前服务的jvm基本信息。

### 项目文档

1. [Java项目中数据采集方式](./reference/apm_gather.md)
2. [怎样将数据导入到MySql](./reference/apm_mysql.md)
3. [怎样将数据导入到ElasticSearch](./reference/apm_elasticsearch.md)
4. [基于MySql数据源的展示与预警平台]()
5. [基于ElasticSearch搭建Grafana展示平台]()



### 交流

QQ群：365133362 群名称：互联网从业者    