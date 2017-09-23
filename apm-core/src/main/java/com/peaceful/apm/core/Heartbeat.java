package com.peaceful.apm.core;

/**
 * Created by wangjun38 on 2017/9/17.
 */
public class Heartbeat {

    private String tag; // 监控对象，可以是一个url对象，一个jvm对象
    private String metric; // 监控指标
    private double value; // 指标值
    private long createTime; // 数据采集时间

    public Heartbeat(String tag) {
        this.tag = tag;
        this.createTime = System.currentTimeMillis() / 1000;
    }

    public void upload(String metric, double value) {
        Heartbeat copy = new Heartbeat(tag);
        copy.createTime = this.createTime;
        copy.metric = metric;
        copy.value = value;
        copy.value = value;
        HeartBeatService.uploadHeartbeatData(copy);
    }

    protected String getMetric() {
        return metric;
    }

    protected String getTag() {
        return tag;
    }

    protected double getValue() {
        return value;
    }

    protected long getCreateTime() {
        return createTime;
    }
}
