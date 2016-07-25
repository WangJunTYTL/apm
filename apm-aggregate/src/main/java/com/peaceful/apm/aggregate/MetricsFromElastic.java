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
        ElasticLog4jAppender appenderLog4j = ElasticLog4jAppender.getInstance();
        ElasticLogbackAppender appenderLogback = ElasticLogbackAppender.getInstance();
        if (appenderLog4j != null) {
            appenderLog4j.save(timingStatisticsMap, interval, timeUnit);
        } else if (appenderLogback != null) {
            appenderLogback.save(timingStatisticsMap, interval, timeUnit);
        }
    }
}
