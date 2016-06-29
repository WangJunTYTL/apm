# Alert

通用的简单表达式预警规则定义。

```
// 定义监控数据来源
MetricCallback metricCallback = new MetricCallback() {
    @Override
    public Map<String, String> get() {
        Map<String, String> metrics = Maps.newHashMap();
        metrics.put("connection.count", "102");
        metrics.put("connection.release.time", "2");
        return metrics;
    }
};
// 下发消息内容
Message message = new Message("${tag}", "当前连接数${connection.count},连接释放时间${connection.release.time}", MsgLevelEnum.WARN);

// 定义预警规则并启动
Alert.newItem("db").term("${connection.count}>100&${connection.release.time}>1")
        .notice(message).to("wangjuntytl@163.com")
        .interval(5, TimeUnit.SECONDS).metric(metricCallback).build();
```