package com.peaceful.apm.core;

/**
 * 记录一个指标对应的数值
 *
 * Created by wangjun38 on 2017/9/25.
 */
public class Counter {

    private String tag; // 指标tag
    private String metric; // 指标名称
    private double value; // 指标值
    private long createTime; // unix time

    public Counter(String tag, String metric, double value) {
        this.tag = tag;
        this.metric = metric;
        this.value = value;
        this.createTime = System.currentTimeMillis() / 1000;
    }

    public String getTag() {
        return tag;
    }

    public String getMetric() {
        return metric;
    }

    public double getValue() {
        return value;
    }

    public long getCreateTime() {
        return createTime;
    }
}
