package com.peaceful.apm.alert;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * Alert模块公用线程池
 *
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class AlertExecutor {

    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("apm-alert-%d")
            .setDaemon(true)
            .build();
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newScheduledThreadPool(5, threadFactoryBuilder);


}
