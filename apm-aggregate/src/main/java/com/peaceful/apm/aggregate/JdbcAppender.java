package com.peaceful.apm.aggregate;

import org.perf4j.TimingStatistics;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/7/22
 */
public interface JdbcAppender {

    MetricsSet select(String sql, String tag, long from, long to);

    List<String> selectTags();

    void save(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit);
}
