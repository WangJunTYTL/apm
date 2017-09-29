package com.peaceful.apm.core.logback;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;

import com.google.common.base.Throwables;

import com.peaceful.apm.core.helper.Log;
import com.peaceful.apm.core.helper.NetHelper;
import com.peaceful.apm.core.store.MetricElasticStore;

import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * 如果你使用logback组件，通过该appender可以把数据写入到elasticsearch 必须作为 {@link org.perf4j.logback.AsyncCoalescingStatisticsAppender}的子Appender
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class ElasticLogbackAppender extends AppenderBase<LoggingEvent> {

    private String host;
    private int port = 9300;
    private String clusterName = "elasticsearch";
    private String indexPrefix;

    private MetricElasticStore metricsFromElastic;

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
        if (!NetHelper.pingIpPort(host, port, 3)) {
            Log.warn("ElasticSearch connection fail, host:" + host + " port:" + port);
            return;
        }
        metricsFromElastic = new MetricElasticStore(host, port, clusterName, indexPrefix);
        super.start();
    }

    @Override
    public void stop() {
        if (metricsFromElastic != null)
            metricsFromElastic.close();
        super.stop();
    }
}
