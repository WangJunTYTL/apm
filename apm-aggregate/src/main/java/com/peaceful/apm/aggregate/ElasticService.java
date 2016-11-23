package com.peaceful.apm.aggregate;

import com.google.common.base.Throwables;
import com.peaceful.boot.common.helper.DateHelper;
import com.peaceful.boot.common.helper.NetHelper;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.perf4j.TimingStatistics;

import java.math.BigDecimal;
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
public class ElasticService implements AppenderService {

    private static SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.getDefault());

    private Client client;
    private String host;
    private int port;
    private String clusterName;
    private String indexPrefix;
    private final static int NEW = 0;
    private final static int START = 1;
    private final static int STOP = 2;
    private int state = NEW;

    public ElasticService(String host, int port, String clusterName, String indexPrefix) {
        this.host = host;
        this.port = port;
        this.clusterName = clusterName;
        this.indexPrefix = indexPrefix;
    }


    @Override
    public void saveStatisticsData(Map<String, TimingStatistics> timingStatisticsSortedMap, long interval, TimeUnit timeUnit) {
        if (state != START) {
            Settings settings = ImmutableSettings.settingsBuilder()
                    .put("cluster.name", clusterName).build();
            client = new TransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(host, port));
            state = START;
        }
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
                client.prepareIndex(indexPrefix + DateHelper.getStringByPattern(new Date(), "yyyy.MM"), "apm")
                        .setSource(data)
                        .execute()
                        .actionGet();
            } catch (Exception e) {
                Throwables.propagate(e);
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
