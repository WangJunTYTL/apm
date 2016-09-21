将数据导入到ElasticSearch
------------
虽然MySql足够稳定和灵活，但不适合长期存储海量数据且ElasticSearch提供强大聚合功能，可以满足监控系统查常按照时间查询聚合的查询，
apm提供导入ElasticSearch的appender组件类，支持1.x版本的es客户端，你可以加入下面依赖

```
<dependency>
    <groupId>com.peaceful</groupId>
    <artifactId>apm-aggregate</artifactId>
    <version>2.6-SNAPSHOT</version>
</dependency>
```
然后在日志文件配置以下信息

```
<appender name="ESAppender"
          class="com.peaceful.apm.aggregate.appender.ElasticLogbackAppender">
    <param name="host" value="localhost"></param>
    <param name="port" value="9300"></param>
    <param name="indexPrefix" value="apm-"></param>
    <param name="clusterName" value="elasticsearch"></param>
</appender>
```
除配置es的连接地址、端口和集群名外，你还需要配置indexPrefix，然参数的意义是:按照索引前缀和当前的月份创建索引,如下索引格式

![alt text](./images/apm_index.png)
  
另外在apm自动创建索引前，最好创建索引模板，主要是为了避免tag和hostname字段分分词器划分成不同的term

```
{
  "order": 0,
  "template": "apm*",
  "settings": {
    "index.number_of_replicas": "0",
    "index.number_of_shards": "1"
  },
  "mappings": {
    "apm": {
      "properties": {
        "tag": {
          "index": "not_analyzed",
          "type": "string"
        },
        "hostname": {
          "index": "not_analyzed",
          "type": "string"
        }
      }
    }
  },
  "aliases": {}
}
```

