package com.peaceful.apm.test.metric;

import com.peaceful.apm.test.appender.MessageAppender;
import com.peaceful.apm.test.appender.MessageAppenderManager;

import lombok.Getter;
import lombok.Setter;

/**
 * Metric目标：统计一块代码块、一个方法或一个服务调用的多个指标：
 *
 * Count:调用次数
 * Mean:平均响应时间
 * Max:最大响应时间
 * Min:最小响应时间
 *
 * Metric使用方式
 *
 * StopWatch stopWatch = APM.createStopWatch();
 * stopWatch.start('tag');
 * do something...
 * stopWatch.end();
 *
 *
 * 说明:
 * StopWatch可多次使用，每次调用start方法时都会开始计时
 *
 *
 * Created by wangjun38 on 2018/1/27.
 */

public class StopWatch {


    @Getter
    private String tag;
    private long startTime;
    private long elapsedTime;
    @Setter
    private WatchType watchType = WatchType.COUNT;

    private static final long NANOS_IN_A_MILLI = 1000000L;


    private static MessageAppender messageChannel = MessageAppenderManager.getStopWatchMessageAppender();

    public void start(String tag) {
        this.tag = tag;
        startTime = System.nanoTime();
    }

    public void end() {
        lap(tag);
        elapsedTime = System.nanoTime() - startTime;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void lap(String tag) {
        if (watchType == null) watchType = WatchType.COUNT;
        StringBuilder builder = new StringBuilder();
        builder.append("start[").append(startTime/NANOS_IN_A_MILLI).append("] ");
        builder.append("time[").append(System.nanoTime() - startTime).append("] ");
        builder.append("tag[").append(tag).append("] ");
        builder.append("mark[").append(watchType.getTypeCode()).append("]");
        messageChannel.send(builder.toString());
    }


}
