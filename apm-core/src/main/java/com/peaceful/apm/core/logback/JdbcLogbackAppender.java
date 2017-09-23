package com.peaceful.apm.core.logback;

import com.google.common.base.Throwables;

import com.peaceful.apm.core.MetricMySqlStore;

import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * 如果使用logback组件，使用该appender通过jdbc输出到db
 * 已验证支持Mysql、SQLite
 * <p>
 * 必须作为 {@link org.perf4j.logback.AsyncCoalescingStatisticsAppender}的 subAppender
 *
 * @author WangJun
 * @version 1.0 16/6/24
 */
public class JdbcLogbackAppender extends AppenderBase<ch.qos.logback.classic.spi.LoggingEvent> {

    private String url;
    private String user;
    private String password;
    private String driver = "com.mysql.jdbc.Driver";
    private String service = "Unknown";
    private MetricMySqlStore dbService;

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setService(String service) {
        this.service = service;
    }

    public void save(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        if (timingStatisticsMap == null || timingStatisticsMap.isEmpty()) {
            return;
        }
        dbService.saveStatisticsData(timingStatisticsMap, interval, timeUnit);
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if ((loggingEvent.getArgumentArray() != null)
                && (loggingEvent.getArgumentArray().length > 0)) {
            Object logMessage = loggingEvent.getArgumentArray()[0];
            if (logMessage instanceof GroupedTimingStatistics) {
                GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
                try {
                    save(statistics.getStatisticsByTag(), statistics.getStopTime() - statistics.getStartTime(), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    addError(Throwables.getStackTraceAsString(e));
                }
            }
        }
    }

    @Override
    public void start() {
        dbService = new MetricMySqlStore(url, user, password, driver, service);
        super.start();
    }
}
