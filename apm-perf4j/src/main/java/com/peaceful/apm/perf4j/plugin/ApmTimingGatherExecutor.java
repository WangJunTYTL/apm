package com.peaceful.apm.perf4j.plugin;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.peaceful.apm.perf4j.AppenderService;
import com.peaceful.apm.perf4j.helper.SpiHelper;
import com.peaceful.apm.perf4j.plugin.spi.TimingAggregate;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class ApmTimingGatherExecutor {


    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("apm-timing-gather-%d")
            .setDaemon(true)
            .build();
    private static List<AppenderService> serviceList = new CopyOnWriteArrayList<>();
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);

    public static synchronized void loadToJvm(AppenderService service) {
        for (AppenderService appenderService:serviceList){
            if (appenderService == service){
                return;
            }
        }
        List<TimingAggregate> timingAggregates = SpiHelper.selectList(TimingAggregate.class);
        if (timingAggregates != null && !timingAggregates.isEmpty()) {
            SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new GatherJvm(service, 1, TimeUnit.MINUTES, timingAggregates), 1, 1, TimeUnit.MINUTES);
        }
    }

    private static class GatherJvm implements Runnable {
        private int interval;
        private TimeUnit timeUnit;
        private List<TimingAggregate> timingAggregates;
        private AppenderService service;

        public GatherJvm(AppenderService service, int interval, TimeUnit timeUnit, List<TimingAggregate> timingAggregates) {
            this.service = service;
            this.interval = interval;
            this.timeUnit = timeUnit;
            this.timingAggregates = timingAggregates;
        }

        public void run() {
            try {
                for (TimingAggregate aggregate : timingAggregates) {
                    service.saveStatisticsData(aggregate.get(), interval, timeUnit);
                }
            } catch (Exception e) {
                System.err.println(Throwables.getStackTraceAsString(e));
            }
        }
    }

}
