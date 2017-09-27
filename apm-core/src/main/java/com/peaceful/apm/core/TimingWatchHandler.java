package com.peaceful.apm.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import com.peaceful.apm.core.helper.Log;
import com.typesafe.config.ConfigFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun38 on 2017/9/17.
 */
public class TimingWatchHandler {

    private LinkedBlockingQueue<Counter> buffer = new LinkedBlockingQueue<>(1024);
    private Set<Class> watchSet = Sets.newHashSet();
    private volatile boolean isRun = false;


    public TimingWatchHandler() {
        synchronized (this) {
            if (isRun) {
                return;
            }
            loadToJvm();
            isRun = true;
        }
    }

    public boolean isStart() {
        return isRun;
    }

    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("apm-timing-watch-%d")
            .setDaemon(true)
            .build();
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);

    public synchronized void loadToJvm() {
        List<String> monitors = ConfigFactory.load("apm").getStringList("watch.monitor");
        if (monitors != null && !monitors.isEmpty()) {
            for (final String watch : monitors) {
                if (!watchSet.contains(watch)) {
                    try {
                        final TimingWatch timingWatch = (TimingWatch) Class.forName(watch).newInstance();
                        SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(new Runnable() {
                            @Override
                            public void run() {
                                enqueue(timingWatch.lap());
                            }
                        }, 1, timingWatch.interval(), TimeUnit.MINUTES);
                        Log.info("timing watch start:" + watch.getClass() + " watch interval:" + timingWatch.interval() + "min");
                    } catch (Exception e) {
                        Log.error(e);
                        continue;
                    }
                }
            }
        }
    }

    public List<Counter> getNext() {
        try {
            List<Counter> counters = Lists.newLinkedList();
            long startTime = System.currentTimeMillis();
            long remain = 60;
            while (remain > 0) {
                Counter beatData = buffer.poll(remain, TimeUnit.SECONDS);
                if (beatData != null) {
                    counters.add(beatData);
                }
                remain = remain - (System.currentTimeMillis() - startTime) / 1000;
            }
            return counters;
        } catch (Exception e) {
            Log.warn("upload counter data fail");
            return null;
        }
    }

    protected void enqueue(List<Counter> counters) {
        if (counters == null) return;
        for (Counter counter : counters) {
            if (!buffer.offer(counter)) {
                Log.warn("metric data is too much , will throw some");
            }
        }
    }

}
