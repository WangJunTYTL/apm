package com.peaceful.apm.core.store;

import com.google.common.base.Throwables;

import com.peaceful.apm.core.MetricStore;
import com.peaceful.apm.core.helper.DateHelper;
import com.peaceful.apm.core.helper.Log;
import com.peaceful.apm.core.helper.NetHelper;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.perf4j.TimingStatistics;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class MetricElasticStore implements MetricStore {

    private static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault());

    private Client client;
    private String host;
    private int port;
    private String clusterName;
    private String indexPrefix;
    private final static int NEW = 0;
    private final static int START = 1;
    private final static int STOP = 2;
    private int state = NEW;

    public MetricElasticStore(String host, int port, String clusterName, String indexPrefix) {
        this.host = host;
        this.port = port;
        this.clusterName = clusterName;
        this.indexPrefix = indexPrefix;
    }

    private synchronized boolean isStart() {
        if (state == NEW) {
            Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
            try {
                client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
            } catch (UnknownHostException e) {
                Log.warn("elasticsearch start error:" + Throwables.getStackTraceAsString(e));
                state = STOP;
            }
            state = START;
        }
        if (state == START) {
            return true;
        }
        return false;
    }


    @Override
    public void saveStatisticsData(Map<String, TimingStatistics> timingStatisticsSortedMap, long interval, TimeUnit timeUnit) {
        if (!isStart()) return;
        for (String tag : timingStatisticsSortedMap.keySet()) {
            TimingStatistics timingStatistics = timingStatisticsSortedMap.get(tag);
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("tag", tag.toLowerCase());
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
            data.put("@timestamp", ISO8601DATEFORMAT.format(new Date()));
            try {
                client.prepareIndex(indexPrefix + DateHelper.getStringByPattern(new Date(), "yyyy.MM"), "apm")
                        .setSource(data)
                        .execute()
                        .actionGet();
            } catch (Exception e) {
                Log.debug(e);
            }
        }
    }


    public void close() {
        if (state == START) {
            state = STOP;
            this.client.close();
        }
    }
}
