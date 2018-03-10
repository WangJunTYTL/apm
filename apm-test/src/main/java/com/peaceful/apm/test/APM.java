package com.peaceful.apm.test;

import com.peaceful.apm.test.metric.StopWatch;

/**
 * Created by wangjun38 on 2018/1/27.
 */
public class APM {

    public static StopWatch createStopWatch() {
        StopWatch stopWatch = new StopWatch();
        return stopWatch;
    }
}
