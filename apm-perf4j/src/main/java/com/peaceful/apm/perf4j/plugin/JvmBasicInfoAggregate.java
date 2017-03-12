package com.peaceful.apm.perf4j.plugin;

import com.google.common.collect.Maps;
import com.peaceful.apm.perf4j.plugin.spi.TimingAggregate;
import org.perf4j.TimingStatistics;

import java.lang.management.*;
import java.util.Map;

/**
 * 获取jvm的基本信息
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class JvmBasicInfoAggregate implements TimingAggregate {

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    public Map<String, TimingStatistics> get() {
        Map<String, TimingStatistics> statisticsMap = Maps.newHashMap();
        MemoryUsage memoryUsage = null;
        memoryUsage = memoryMXBean.getHeapMemoryUsage();
        TimingStatistics useHeap = new TimingStatistics(0, 0, 0, 0, Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024)));
        statisticsMap.put("jvm.used.heap", useHeap);

        memoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        TimingStatistics useNonHeap = new TimingStatistics(0, 0, 0, 0, Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024)));
        statisticsMap.put("jvm.used.non.heap", useNonHeap);

        TimingStatistics threadCount = new TimingStatistics(0, 0, 0, 0, threadMXBean.getThreadCount());
        statisticsMap.put("jvm.thread.count", threadCount);

        long garbageCollectionTime = 0;
        int gcCount = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory
                .getGarbageCollectorMXBeans()) {
            garbageCollectionTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
        }
        TimingStatistics gc = new TimingStatistics(garbageCollectionTime, 0, 0, 0, gcCount);
        statisticsMap.put("jvm.gc", gc);
        return statisticsMap;
    }
}
