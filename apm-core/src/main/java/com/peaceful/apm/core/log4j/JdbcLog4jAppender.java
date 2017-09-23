package com.peaceful.apm.core.log4j;

import com.google.common.base.Throwables;

import com.peaceful.apm.core.MetricMySqlStore;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 如果使用log4j组件，使用该appender通过jdbc输出到db
 * 已验证支持Mysql、SQLite
 * <p>
 * 必须作为 {@link org.perf4j.log4j.AsyncCoalescingStatisticsAppender}的 subAppender
 *
 * @author WangJun
 * @version 1.0 16/6/24
 */
public class JdbcLog4jAppender extends AppenderSkeleton {

    private String url;
    private String user;
    private String password;
    private String driver = "com.mysql.jdbc.Driver";
    private String service = "Unknown";

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

    private MetricMySqlStore dbService;

    @Override
    public void activateOptions() {
        if (StringUtils.isBlank(driver)) {
            return;
        }
        dbService = new MetricMySqlStore(url,user,password,driver,service);
    }


    public void save(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        if (timingStatisticsMap == null || timingStatisticsMap.isEmpty()) {
            return;
        }
        dbService.saveStatisticsData(timingStatisticsMap,interval,timeUnit);
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        Object logMessage = loggingEvent.getMessage();
        if (logMessage instanceof GroupedTimingStatistics) {
            GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
            try {
                save(statistics.getStatisticsByTag(), statistics.getStopTime() - statistics.getStartTime(), TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                getErrorHandler().error(Throwables.getStackTraceAsString(e));
            }
        }
    }

    @Override
    public void close() {
        this.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

}
