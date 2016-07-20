package com.peaceful.apm.aggregate;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.common.base.Throwables;
import com.peaceful.boot.common.helper.DateHelper;
import com.peaceful.boot.common.helper.NetHelper;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * perf4j for es appender
 * 必须作为 {@link org.perf4j.log4j.AsyncCoalescingStatisticsAppender}的 subAppender
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class ElasticLogbackAppender extends AppenderBase<LoggingEvent> {

    private String url;
    private int port = 9300;
    private String clusterName = "elasticsearch";
    private Client client;
    private String indexPrefix;
    private static final int OK = 1;
    private static final int FAIL = 2;
    private static final int NOT_INIT = 0;
    private int state = NOT_INIT;
    private static ElasticLogbackAppender instance;
    private static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

    public void setUrl(String url) {
        this.url = url;
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

    public void activateOptions() {
        if (StringUtils.isBlank(url)) {
            state = FAIL;
            System.err.println("elasticsearch url must be set");
        }
        if (StringUtils.isBlank(indexPrefix)) {
            state = FAIL;
            System.err.println("elasticsearch index prefix must be set");
        }
    }

    public void save(Map<String, TimingStatistics> timingStatisticsSortedMap, long interval, TimeUnit timeUnit) {
        for (String tag : timingStatisticsSortedMap.keySet()) {
            TimingStatistics timingStatistics = timingStatisticsSortedMap.get(tag);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("tag", tag);
            data.put("count", timingStatistics.getCount());
            BigDecimal bg = new BigDecimal(timingStatistics.getMean());
            double mean = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.put("mean", mean);
            data.put("min", timingStatistics.getMin());
            data.put("max", timingStatistics.getMax());
            bg = new BigDecimal(timingStatistics.getStandardDeviation());
            double std = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            data.put("std", std);
            // 自动追加下面数据
            data.put("hostname", NetHelper.getHostname());
            data.put("timestamp", ISO8601DATEFORMAT.format(new Date()));
            try {
                IndexResponse response = client.prepareIndex(indexPrefix + DateHelper.getStringByPattern(new Date(), "yyyyMM"), "perf4j")
                        .setSource(data)
                        .execute()
                        .actionGet();
                System.err.println("upload data to elasticsearch response ->" + response);
            } catch (Exception e) {
                System.err.println("upload data to elasticsearch fail ->" + e);
            }
        }

    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if (state == FAIL) {
            return;
        } else if (state == NOT_INIT) {
            try {
                activateOptions();
                Settings settings = ImmutableSettings.settingsBuilder()
                        .put("cluster.name", clusterName).build();
                client = new TransportClient(settings)
                        .addTransportAddress(new InetSocketTransportAddress(url, port));
                instance = this;
                ApmTimingGatherExecutor.loadToJvm();
                state = OK;
            } catch (Exception e) {
                state = FAIL;
                Throwables.propagate(e);
            }
        }
        if ((loggingEvent.getArgumentArray() != null)
                && (loggingEvent.getArgumentArray().length > 0)) {
            Object logMessage = loggingEvent.getArgumentArray()[0];
            if (logMessage instanceof GroupedTimingStatistics) {
                GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;

                try {
                    SortedMap<String, TimingStatistics> timingStatisticsSortedMap = statistics.getStatisticsByTag();
                    save(timingStatisticsSortedMap, statistics.getStopTime() - statistics.getStartTime(), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    System.err.println(Throwables.getStackTraceAsString(e));
                }
            }
        }
    }

    public static ElasticLogbackAppender getInstance() {
        return instance;
    }
}
