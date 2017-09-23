package com.peaceful.apm.dashboard.datasource;

/**
 * @author WangJun
 * @version 1.0 16/6/29
 */
public class MetricsForHeartbeat {
    public String metric; // metric
    public double value; //  指定时间一个interval周期内的请求次数
    public long createTime; // 数据创建时间

}
