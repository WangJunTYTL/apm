package com.peaceful.apm.alert;

import com.google.common.collect.Maps;
import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.MsgLevelEnum;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class AlertTest {

    @org.junit.Test
    public void newItem() throws Exception {
        Message message = new Message("${tag}", "当前连接数${connection.count},连接释放时间${connection.release.time}", MsgLevelEnum.WARN);
        MetricCallback metricCallback = new MetricCallback() {
            @Override
            public Map<String, String> get() {
                Map<String, String> metrics = Maps.newHashMap();
                metrics.put("connection.count", "102");
                metrics.put("connection.release.time", "2");
                return metrics;
            }
        };
        Alert.newItem("db").term("${connection.count}>100&${connection.release.time}>1")
                .notice(message).to("wangjuntytl@163.com")
                .interval(1, TimeUnit.MINUTES).metric(metricCallback).build();

        TimeUnit.MINUTES.sleep(2);
    }

}