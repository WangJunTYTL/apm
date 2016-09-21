package com.peaceful.apm.aggregate.appender;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.common.base.Throwables;
import com.peaceful.apm.aggregate.ElasticService;
import com.peaceful.apm.aggregate.plugin.ApmTimingGatherExecutor;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 如果你使用logback组件，通过该appender可以把数据写入到elasticsearch
 * 必须作为 {@link org.perf4j.logback.AsyncCoalescingStatisticsAppender}的子Appender
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class ElasticLogbackAppender extends AppenderBase<LoggingEvent> {

    private String host;
    private int port = 9300;
    private String clusterName = "elasticsearch";
    private String indexPrefix;

    private ElasticService metricsFromElastic;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIndexPrefix(String indexPrefix) {
        this.indexPrefix = indexPrefix;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }


    public void save(Map<String, TimingStatistics> timingStatisticsSortedMap, long interval, TimeUnit timeUnit) {
        metricsFromElastic.saveStatisticsData(timingStatisticsSortedMap, interval, timeUnit);
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if ((loggingEvent.getArgumentArray() != null)
                && (loggingEvent.getArgumentArray().length > 0)) {
            Object logMessage = loggingEvent.getArgumentArray()[0];
            if (logMessage instanceof GroupedTimingStatistics) {
                GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
                try {
                    SortedMap<String, TimingStatistics> timingStatisticsSortedMap = statistics.getStatisticsByTag();
                    save(timingStatisticsSortedMap, statistics.getStopTime() - statistics.getStartTime(), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    addError(Throwables.getStackTraceAsString(e));
                }
            }
        }
    }

    @Override
    public void start() {
        metricsFromElastic = new ElasticService(host, port, clusterName, indexPrefix);
        ApmTimingGatherExecutor.loadToJvm(metricsFromElastic);
        super.start();
    }

    @Override
    public void stop() {
        metricsFromElastic.close();
        super.stop();
    }
}
