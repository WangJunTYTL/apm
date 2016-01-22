package org.perf4j.jvm;

import java.lang.management.*;

/**
 * Created by wangjun on 16/1/21.
 */
public class JVMInfo {

    private static ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
    private static MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    public static String UsedHeap = "UsedHeap";
    public static String UsedNonHeap = "UsedNonHeap";
    public static String Thread = "Thread";
    public static String GCTime = "GCTime";
    public static String GCCount = "GCCount";

    public static JVMGraphData getMemoryInfo(int type) {
        JVMGraphData jvmGraphData = new JVMGraphData();
        MemoryUsage memoryUsage = null;
        if (type == 0) {
            memoryUsage = memoryMXBean.getHeapMemoryUsage();
            jvmGraphData.tag = "UsedHeap";
        } else if (type == 1) {
            memoryUsage = memoryMXBean.getNonHeapMemoryUsage();
            jvmGraphData.tag = "UsedNonHeap";
        }
        jvmGraphData.count = String.valueOf(memoryUsage.getUsed()/1024/1024);
        jvmGraphData.timestamp = System.currentTimeMillis();
        return jvmGraphData;
    }

    public static JVMGraphData getThreadInfo() {
        JVMGraphData jvmGraphData = new JVMGraphData();
        jvmGraphData.tag = "Thread";
        jvmGraphData.count = String.valueOf(threadMXBean.getThreadCount());
        jvmGraphData.timestamp = System.currentTimeMillis();
        return jvmGraphData;
    }

    public static JVMGraphData getGCInfo(int type) {
        long garbageCollectionTime = 0;
        int gcCount = 0;
        for (final GarbageCollectorMXBean garbageCollector : ManagementFactory
                .getGarbageCollectorMXBeans()) {
            garbageCollectionTime += garbageCollector.getCollectionTime();
            gcCount += garbageCollector.getCollectionCount();
        }
        JVMGraphData jvmGraphData = new JVMGraphData();
        if (type == 0) {
            jvmGraphData.tag = "GCTime";
            jvmGraphData.count = String.valueOf(garbageCollectionTime);
        } else {
            jvmGraphData.tag = "GCCount";
            jvmGraphData.count = String.valueOf(gcCount);
        }
        jvmGraphData.timestamp = System.currentTimeMillis();
        return jvmGraphData;
    }




}
