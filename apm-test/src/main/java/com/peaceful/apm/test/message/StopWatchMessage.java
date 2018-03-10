package com.peaceful.apm.test.message;

import lombok.Data;

/**
 * Created by wangjun38 on 2018/1/27.
 */
@Data
public class StopWatchMessage {

    private String tag;
    private long start;
    private long time;
}
