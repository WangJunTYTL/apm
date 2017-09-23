package com.peaceful.apm.dashboard.datasource;

import java.util.LinkedList;
import java.util.List;

/**
 * 性能数据结果集合
 *
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricsSet {

    public String tag; // metric
    public long interval; // 聚合周期
    public String hostname; // 数据来源机器
    public List<String> series; // 时间序列集合
    public List<Long> counts; // 一个interval周期的执行次数
    public List<String> tpss; // 一个interval周期的执行次数
    public List<Double> means; // 一个interval周期的平均响应时间
    public List<Long> mins; // 一个interval周期内的最小响应时间
    public List<Long> maxs; // 一个interval周期内的最大响应时间
    public List<Double> stds; // 一个interval周期请求次数方差

    public MetricsSet(String tag, long interval, String hostname) {
        this.tag = tag;
        this.interval = interval;
        this.hostname = hostname;
        this.series = new LinkedList<>();
        this.counts = new LinkedList<>();
        this.tpss = new LinkedList<>();
        this.means = new LinkedList<>();
        this.mins = new LinkedList<>();
        this.maxs = new LinkedList<>();
        this.stds = new LinkedList<>();
    }

}
