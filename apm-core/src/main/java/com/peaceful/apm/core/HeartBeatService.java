package com.peaceful.apm.core;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

import com.peaceful.apm.core.helper.Log;
import com.peaceful.apm.core.helper.SpiHelper;

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
public abstract class HeartBeatService {

    private static LinkedBlockingQueue<Heartbeat> buffer = new LinkedBlockingQueue<>(1024);
    private static Set<Class> tasks = Sets.newHashSet();

    public static List<Heartbeat> getNext() {
        try {
            List<Heartbeat> heartBeatDataList = Lists.newLinkedList();
            long startTime = System.currentTimeMillis();
            long remain = 60;
            while (remain > 0) {
                Heartbeat beatData = buffer.poll(remain, TimeUnit.SECONDS);
                if (beatData != null) {
                    heartBeatDataList.add(beatData);
                }
                remain = remain - (System.currentTimeMillis() - startTime) / 1000;
            }
            return heartBeatDataList;
        } catch (Exception e) {
            // ignore
            Log.debug("upload heartbeat data fail");
            return null;
        }
    }

    protected static void uploadHeartbeatData(Heartbeat heartBeatData) {
        if (!buffer.offer(heartBeatData)) {
            // .ignore
            Log.debug("upload heartbeat data fail");
        }
    }

    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("apm-heartbeat-%d")
            .setDaemon(true)
            .build();
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);

    public static synchronized void loadToJvm() {
        List<HeartbeatTask> heartbeatUploads = SpiHelper.selectList(HeartbeatTask.class);
        if (heartbeatUploads != null && !heartbeatUploads.isEmpty()) {
            for (HeartbeatTask heartbeatUpload : heartbeatUploads) {
                if (!tasks.contains(heartbeatUpload.getClass())) {
                    SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(heartbeatUpload, 1, heartbeatUpload.rate(), TimeUnit.MINUTES);
                    Log.info("heartbeat task start:" + heartbeatUpload.getClass() + " upload rate:" + heartbeatUpload.rate() + "min");
                }
            }
        }
    }

}
