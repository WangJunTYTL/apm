package com.peaceful.apm.dashboard.datasource;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 性能数据结果集合
 *
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricsSetForHeartBeat {

    public String hostname; // 数据来源机器
    public String type; // metric
    public Map<String,MetricsHeartbeatData> metrics; //

    public MetricsSetForHeartBeat(String hostname, String type) {
        this.type = type;
        this.hostname = hostname;
        metrics = Maps.newTreeMap();
    }


}
