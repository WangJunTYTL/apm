package com.peaceful.apm.dashboard.datasource;

/**
 * @author WangJun
 * @version 1.0 16/6/29
 */
public class MetricsForTag {
    public String tag; // metric
    public long interval; // 聚合周期
    public String hostname; // 所在机器hostname
    public long createTime; // 数据创建时间 unix_timestamp
    public long count; //  指定时间一个interval周期内的请求次数
    public double mean; //指定时间一个interval周期内的平均响应时间
    public long min; //指定时间一个interval周期内的最小响应时间
    public long max; //指定时间一个interval周期内的最大响应时间
    public double std; //指定时间一个interval周期内的请求次数方差

}
