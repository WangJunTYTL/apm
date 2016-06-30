数据输出
----------
Perf4j会根据你设定的周期，在本地进行数据聚合，比如建议设置1分钟聚合一次，然后数据可以通过日志组件输出，所以如果我们要想定制数据的输出地，我们需要编写
当前日志组件的Appender。以log4j为例,你需要实现下面方法
```
public class MyAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent loggingEvent) {
        Object logMessage = loggingEvent.getMessage();
        if (logMessage instanceof GroupedTimingStatistics) {
            GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
            try {
                // 保存数据
            } catch (Exception e) {
                getErrorHandler().error(Throwables.getStackTraceAsString(e));
            }
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
```

如果没有特别要求，在apm-aggregate模块默认实现了基于log4j日志组件的数据输出。第一种通过Jdbc方式保存到数据库，如Mysql、SQLite，第二种输出到ElasticSearch，
你可以直接在log4j.xml配置。

## 输出到MySql
```
<appender name="Perf4jJdbcAppender"
          class="com.peaceful.apm.aggregate.JdbcLog4jAppender">
    <param name="url"
           value="jdbc:mysql://127.0.0.1:3306/test3?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false"></param>
    <param name="user" value="root"></param>
    <param name="password" value=""></param>
    <param name="driver" value="com.mysql.jdbc.Driver"></param>
</appender>
```

## 输出到SQLite
```
<appender name="Perf4jSQLiteJdbcAppender"
          class="com.peaceful.apm.aggregate.JdbcLog4jAppender">
    <param name="url" value="jdbc:sqlite:apm.db"></param>
    <param name="driver" value="org.sqlite.JDBC"></param>
</appender>
```

## 输出到ElasticSearch
```
 <appender name="Perf4jESAppender"
           class="com.peaceful.apm.aggregate.ElasticLog4jAppender">
     <param name="url" value="127.0.0.1"></param>
     <param name="port" value="9300"></param>
     <param name="indexPrefix" value="apm-"></param>
     <param name="clusterName" value="elasticsearch"></param>
 </appender>
```
## 建议输出

如果你需要使用附带的dashboard，你需要将数据保存到数据库中，比如MySQL与SQLite，建议保存到SQLite，这样你的性能数据会只保存到本地文件，另外附带的
dashboard只是用来查看最近的数据，比如支持最近30天的数据查看，如果你要查看一年的性能监控数据，这需要有基于时间聚合的数据库产品，比如InfluxDB、Graphite、
ElasticSearch等












