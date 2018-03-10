package com.peaceful.apm.test.message;

import com.google.common.collect.Maps;

import java.util.Map;

import lombok.Data;

/**
 * Created by wangjun38 on 2018/1/28.
 */
@Data
public class StopWatchMetric {

    private long time;
    private String tag;
    private long count;
    private double mean;
    private double min;
    private double t50;
    private double t75;
    private double t95;
    private double t99;
    private double t999;
    private double max;


    public Map<String, Object> toMap() {
        Map<String, Object> data = Maps.newHashMap();
        data.put("time", time);
        data.put("tag", tag);
        data.put("count", count);
        data.put("mean", mean);
        data.put("min", min);
        data.put("t50", t50);
        data.put("t75", t75);
        data.put("t95", t95);
        data.put("t99", t99);
        data.put("t999", t999);
        data.put("max", max);
        return data;
    }

}
