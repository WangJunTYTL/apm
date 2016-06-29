package com.peaceful.apm.aggregate;

import java.util.LinkedList;
import java.util.List;

/**
 * 性能数据结果集合
 *
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricsSet {

    public String tag;
    public String interval;
    public String hostname;
    public List<String> series;
    public List<Long> counts;
    public List<Double> means;
    public List<Long> mins;
    public List<Long> maxs;
    public List<Double> stds;

    public MetricsSet(String tag, String interval, String hostname) {
        this.series = new LinkedList<>();
        this.counts = new LinkedList<>();
        this.means = new LinkedList<>();
        this.mins = new LinkedList<>();
        this.maxs = new LinkedList<>();
        this.stds = new LinkedList<>();
    }

}
