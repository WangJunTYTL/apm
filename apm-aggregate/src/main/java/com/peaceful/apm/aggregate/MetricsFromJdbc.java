package com.peaceful.apm.aggregate;

import org.apache.log4j.helpers.LogLog;
import org.perf4j.TimingStatistics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.peaceful.apm.aggregate.JdbcLog4jAppender.TABLE;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricsFromJdbc {

    private final static String QUERY_SQL = "select * from " + TABLE + " where hostname = ? and tag = ? and create_time between ? and ?";
    private final static String QUERY_TAG__SQL = "select DISTINCT (tag) from " + TABLE;

    /**
     * 选择给定时间的最新的一条数据
     *
     * @param tag
     * @param from 单位ms
     * @param to   单位ms
     */
    public static MetricsForTag selectOneMetrics(String tag, long from, long to) {
        JdbcAppender instance = JdbcLog4jAppender.getInstance();
        if (instance == null){
            instance = JdbcLogbackAppender.getInstance();
        }
        if (instance != null) {
            MetricsSet metricsSet = instance.select(QUERY_SQL + " order by create_time desc limit 1", tag, from, to);
            if (metricsSet != null) {
                MetricsForTag metricsForTag = new MetricsForTag();
                metricsForTag.tag = metricsSet.tag;
                metricsForTag.count = metricsSet.counts.get(0);
                metricsForTag.mean = metricsSet.means.get(0);
                metricsForTag.hostname = metricsSet.hostname;
                metricsForTag.interval = metricsSet.interval;
                metricsForTag.createTime = metricsSet.series.get(0);
                metricsForTag.min = metricsSet.mins.get(0);
                metricsForTag.max = metricsSet.maxs.get(0);
                metricsForTag.std = metricsSet.stds.get(0);
                return metricsForTag;
            }
        }
        return null;
    }

    /**
     * 治理只是个临时方法，手动插入性能数据到Jdbc的连接库中
     *
     * @param timingStatisticsMap 插入数据
     * @param interval            数据分析周期时长
     * @param timeUnit            时间单位标识
     */
    public static void insertMetrics(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        JdbcAppender appender = JdbcLog4jAppender.getInstance();
        if (appender == null){
            appender = JdbcLogbackAppender.getInstance();
        }
        if (appender != null) {
            appender.save(timingStatisticsMap, interval, timeUnit);
        } else {
            // .ignore
        }
    }

    /**
     * 获取指定tag的性能数据
     *
     * @param tag
     * @param from ms单位
     * @param to   ms单位
     */
    public static MetricsSet selectMetrics(String tag, long from, long to) {
        JdbcAppender appender = JdbcLog4jAppender.getInstance();
        if (appender == null){
            appender = JdbcLogbackAppender.getInstance();
        }
        if (appender != null) {
            return appender.select(QUERY_SQL + " order by create_time asc", tag, from, to);
        } else {
            LogLog.error("JdbcAppender is closed");
        }
        return null;
    }

    public static List<String> selectMetricsTag() {
        JdbcAppender appender = JdbcLog4jAppender.getInstance();
        if (appender == null){
            appender = JdbcLogbackAppender.getInstance();
        }
        if (appender != null) {
            return appender.selectTags();
        } else {
            LogLog.error("JdbcAppender is closed");
        }
        return null;
    }

}
