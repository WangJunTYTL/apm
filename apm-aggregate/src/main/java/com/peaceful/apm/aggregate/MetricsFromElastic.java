package com.peaceful.apm.aggregate;

import org.apache.log4j.helpers.LogLog;
import org.perf4j.TimingStatistics;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricsFromElastic {

    public static void insertMetrics(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        ElasticLog4jAppender appender = ElasticLog4jAppender.getInstance();
        if (appender != null) {
            appender.save(timingStatisticsMap, interval, timeUnit);
        } else {
            // .ignore
        }
    }
}
