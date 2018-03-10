package com.peaceful.apm.test.message;

import com.peaceful.apm.test.metric.WatchType;

import lombok.Data;

/**
 * Created by wangjun38 on 2018/1/27.
 */
@Data
public class StopWatchMessageEvent {

    private String tag;
    private long start;
    private long time;
    private WatchType watchType;

}
