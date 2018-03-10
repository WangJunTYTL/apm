package com.peaceful.apm.dashboard.datasource;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by wangjun38 on 2017/9/17.
 */
public class MetricsHeartbeatData {
    public String tag;
    public List<Double> counts; //
    public List<String> series; // 时间序列集合

    public MetricsHeartbeatData(String tag) {
        this.tag = tag;
        counts = Lists.newLinkedList();
        series = Lists.newLinkedList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MetricsHeartbeatData)) return false;

        MetricsHeartbeatData that = (MetricsHeartbeatData) o;

        return tag.equals(that.tag);
    }

    @Override
    public int hashCode() {
        return tag.hashCode();
    }
}
