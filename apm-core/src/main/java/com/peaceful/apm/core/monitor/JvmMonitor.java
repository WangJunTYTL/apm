package com.peaceful.apm.core.monitor;

import com.peaceful.apm.core.Heartbeat;
import com.peaceful.apm.core.HeartbeatTask;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;

/**
 * 获取jvm的基本信息
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class JvmMonitor extends HeartbeatTask {

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public void upload() {
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        Heartbeat heartBeatData = new Heartbeat("JVM");
        heartBeatData.upload("HeapMemory", Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024)));

        memoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        heartBeatData.upload("NonHeapMemory", Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024)));

        long garbageCollectionTime = 0;
        int gcCount = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory
                .getGarbageCollectorMXBeans()) {
            garbageCollectionTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
        }
        heartBeatData.upload("GCTime", garbageCollectionTime);
        heartBeatData.upload("GCCount", gcCount);

        heartBeatData.upload("ThreadCount", threadMXBean.getThreadCount());
    }

    @Override
    public int rate() {
        return 5; // 5分钟上报一次
    }
}

