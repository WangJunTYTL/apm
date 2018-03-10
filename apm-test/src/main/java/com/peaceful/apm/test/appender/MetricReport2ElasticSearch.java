package com.peaceful.apm.test.appender;


import com.peaceful.apm.test.helper.DateHelper;
import com.peaceful.apm.test.message.StopWatchMetric;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by wangjun38 on 2018/1/28.
 */
public class MetricReport2ElasticSearch extends ElasticSearch2MessageAppender {


    public MetricReport2ElasticSearch(String host, int port, String clusterName) {
        super(host, port, clusterName);
    }

    public void send(Object message) {
        // todo
        SimpleDateFormat ISO8601DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ", Locale.getDefault());
        StopWatchMetric metric = (StopWatchMetric) message;
        Map<String, Object> data = metric.toMap();
        Object time = data.remove("time");
        data.put("@timestamp", ISO8601DATEFORMAT.format(new Date((Long) time)));
        getClient().prepareIndex("apm_" + DateHelper.getStringByPattern(new Date(), "yyyy.MM"), "apm")
                .setSource(data)
                .execute()
                .actionGet();
    }
}
