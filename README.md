# Perf4j

Perf4J 是一个开放源码的性能记录，监测和分析库，可以计算服务的TPS、Mean、Count、Max、Min、StdDev 6种指标值，主要用于企业Java应用程序。

perf4j-zh 是修改部分perf4j源码并基于它进行了二次开发，集性能数据采集、聚合、展示、预警完整的业务级别监控解决方案，另取名**Application Performance Monitor**，简称**APM**，
使perf4j能够被更多企业Java项目使用。


![Alt text](./reference/images/metircs.jpg)

[点击查看在实际业务中的监控效果图](./reference/apm_dashboard.md)


## 特点

* 近实时的精确的数据采集，由Perf4j提供。
* 自定义监控数据的输出地，默认提供file、Jdbc、elasticsearch的输出方式
* 自带dashboard，也可以对接到公司内部的监控系统，比如常用的监控系统ganglia、grafana
* 基于简单表达式的预警规则定义
* 简单、实用、开箱即用，如果你已经用过perf4j你可以轻易使用该项目配置dashboard

## APM说明文档

1. [整体架构](https://raw.githubusercontent.com/WangJunTYTL/perf4j-zh/master/doc/perf4j架构解析.jpg)
1. [数据采集](./reference/apm_gather.md)
2. [数据输出](./reference/apm_aggregate.md)
2. [数据展示](./reference/apm_view.md)
2. [预警...待更新]


## 交流

QQ群：365133362 群名称：互联网从业者
   
   
