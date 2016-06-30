# perf4j

Perf4J 是一个开放源码的性能记录，监测和分析库，可以把根据埋点计算服务的TPS、Mean、Count、Max、Min、StdDev 6种指标值，主要用于企业Java应用程序。

perf4j-zh 是修改部分perf4j源码并加入数据聚合、dashboard展示、预警模块，使perf4j能够被更多企业Java项目使用。


## perf4j-zh、 perf4j

1. perf4j-zh重写per4j的图表渲染，由google的chartApi改为baidu的Echart图表，在google被墙的今天，使其更适合国内的使用行情。
1. 性能数据采集与数据渲染进行分离,数据经过本地聚合后可以保存到数据库、elasticsearch以及自定义数据输出地，方面对接到本公司内部的监控系统
1. 附带用于数据展示的dashboard项目，可以查看各个服务的实时、过去监控数据，快速帮助开发者分析系统的瓶颈、问题,节省人力。
1. 和其它监控项目对比最大的特点，是一款定位于业务级别的监控：简单、实用、开箱即用

这里是在一个实际业务项目中的使用演示:[点击查看](./reference/apm_dashboard.md)


## 快速上手

1. 下载项目：git clone https://github.com/WangJunTYTL/perf4j-zh.git
2. 构建：sh ./build.sh **注意：** 如果你正在使用Windows平台，请按照`build.sh`脚本的步骤手动执行
3. 启动dashboard： cd apm-dashboard &&  mvn jetty:run 启动后访问：[127.0.0.1:8889](http://127.0.0.1:8889)，此时没有任何数据
4. 启动demo演示项目：cd perf4j-demo && mvn jetty:run
5. 模拟请求： 执行sh ./test.sh 产生请求量，
6. 刷新Dashboard查看demo项目的请求TPS和接口响应时间
   
## 版本升级

`注意：`上述构建时会构建一个2.0-SNAPSHOT版本的perf4j包如果你正在使用0.9.16版本或更低版本的包请升级，另外如果你们

1. 增加了`SqlLiteAppender`，该Appender可以支持把按时间单元分析后性能数据结果导入到SQLite文件中，用来查看过去数据
1. 内部图表数据结构改变了，不在调用Google的ChartApi，改为把分析好的性能数据结果以Json格式输出
1. 增加了通过HTTP方式查询性能数据的API接口

最新版本中只有内部功能的更新，不会涉及到用户使用层的接口，所以当你的项目原本就依赖perf4j时，你可以直接把依赖包升级到`2.0-SNAPSHOT`

## perf4j-zh说明文档

有关perf4j的节本介绍，大家可以直接在网上搜索，或者下载后查看 ./perf4j-zh/doc/site/index.html

1. [性能数据采集配置](./doc/perf4j_usage.md)
2. [dashboard配置](./doc/dashboard_usage.md)
3. [perf4j架构解析](https://raw.githubusercontent.com/WangJunTYTL/perf4j-zh/master/doc/perf4j架构解析.jpg)

## 交流

QQ群：365133362 群名称：互联网从业者
   
   
