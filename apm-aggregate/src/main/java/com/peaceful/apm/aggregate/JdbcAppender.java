package com.peaceful.apm.aggregate;

import org.perf4j.TimingStatistics;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/7/22
 */
public interface JdbcAppender {

    /**
     * @param sql
     * @param tag
     * @param from start time
     * @param to   end time
     * @return 获取聚合数据从db
     */
    MetricsSet select(String sql, String tag, long from, long to);

    /**
     * @return 选择所有的tag，如果数据保存时间较长，可以选择根据时间段获得tag
     */
    List<String> selectTags();

    /**
     * 保存数据到db
     *
     * @param timingStatisticsMap 本地聚合的数据
     * @param interval            聚合周期
     * @param timeUnit            所用时间单元
     */
    void save(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit);

    /**
     * @return 可用的连接
     */
    public Connection getConnection();
}
