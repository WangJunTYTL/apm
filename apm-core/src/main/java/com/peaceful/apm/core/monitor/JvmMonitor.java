package com.peaceful.apm.core.monitor;

import com.peaceful.apm.core.Counter;
import com.peaceful.apm.core.TimingWatch;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取jvm的基本信息
 *
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class JvmMonitor extends TimingWatch {

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();

    @Override
    public List<Counter> lap() {
        List<Counter> counters = new ArrayList<Counter>();
        MemoryUsage memoryUsage = memoryMXBean.getHeapMemoryUsage();
        int used = Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024));
        Counter counter = new Counter("JVM", "HeapMemory", used);
        counters.add(counter);

        memoryUsage = memoryMXBean.getNonHeapMemoryUsage();
        used = Integer.valueOf(String.valueOf(memoryUsage.getUsed() / 1024 / 1024));
        counters.add(new Counter("JVM", "NonHeapMemory", used));

        long garbageCollectionTime = 0;
        int gcCount = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory
                .getGarbageCollectorMXBeans()) {
            garbageCollectionTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
        }
        counters.add(new Counter("JVM", "GCTime", garbageCollectionTime));
        counters.add(new Counter("JVM", "GCCount", gcCount));
        counters.add(new Counter("JVM", "ThreadCount", threadMXBean.getThreadCount()));
        return counters;
    }

    @Override
    public int interval() {
        return 5; // 5分钟上报一次
    }
}

