package com.peaceful.apm.test.appender;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * StopWatch消息处理总控
 *
 * StopWatchMessage
 * ||
 * AsyncGroupStatisticsMessageAppender
 * ||
 * AsyncMessageAppender
 * ||
 *
 * Created by wangjun38 on 2018/1/27.
 */
public class MessageAppenderManager {

    private static StopWatchMessageAppender stopWatchMessageAppender;
    public static AtomicBoolean stopWatchAppenderIsStart = new AtomicBoolean(false);


    public static StopWatchMessageAppender getStopWatchMessageAppender() {
        if (stopWatchAppenderIsStart.get()) {
            return stopWatchMessageAppender;
        }
        stopWatchMessageAppender = new StopWatchMessageAppender();
        // 负责将消息按照单位单位时间分割成不同的消息段,并对每一段消息进行统计得出：Count、Mean、Max、Min指标
        AsyncGroupStatisticsMessageAppender groupStatisticsMessageAppender = new AsyncGroupStatisticsMessageAppender(60);


        // 第一次聚合处理结果输出
        AsyncMessageAppender asyncMessaageAppender = new AsyncMessageAppender();
        // 将处理结果输出到控制台
        asyncMessaageAppender.addMessageAppender(new MetricReport2Console());
        // 将处理结果输出到ElasticSearch
        asyncMessaageAppender.addMessageAppender(new MetricReport2ElasticSearch("127.0.0.1", 9300, "elasticsearch"));

        groupStatisticsMessageAppender.addMessageAppender(asyncMessaageAppender);

        // 将StopWatch采集到的消息，发送给GroupStatistics
        stopWatchMessageAppender.addMessageAppender(groupStatisticsMessageAppender);

        stopWatchAppenderIsStart.set(true);
        return stopWatchMessageAppender;

    }

}
