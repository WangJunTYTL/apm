package com.peaceful.apm.aggregate;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.peaceful.apm.aggregate.spi.TimingAggregate;
import com.peaceful.boot.common.helper.SpiHelper;
import org.apache.log4j.helpers.LogLog;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/21
 */
public class ApmTimingGatherExecutor {

    private static final int NOT_INIT = 0;
    private static final int OK = 1;
    private static final int FAIL = 2;

    private static int state = NOT_INIT;

    private final static ThreadFactory threadFactoryBuilder = new ThreadFactoryBuilder()
            .setNameFormat("apm-timing-gather-%d")
            .setDaemon(true)
            .build();
    public static final ScheduledExecutorService SCHEDULED_EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor(threadFactoryBuilder);

    public static synchronized void loadToJvm() {
        if (state == NOT_INIT) {
            List<TimingAggregate> timingAggregates = SpiHelper.selectList(TimingAggregate.class);
            if (timingAggregates != null && !timingAggregates.isEmpty()) {
                LogLog.debug("fount " + timingAggregates.size() + " apm aggregate plugin");
                SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new GatherJvm(1, TimeUnit.MINUTES, timingAggregates), 1, 1, TimeUnit.MINUTES);
            }
        }
    }

    private static class GatherJvm implements Runnable {
        private int interval;
        private TimeUnit timeUnit;
        private List<TimingAggregate> timingAggregates;

        public GatherJvm(int interval, TimeUnit timeUnit, List<TimingAggregate> timingAggregates) {
            this.interval = interval;
            this.timeUnit = timeUnit;
            this.timingAggregates = timingAggregates;
        }

        public void run() {
            try {
                for (TimingAggregate aggregate : timingAggregates) {
                    //// TODO: 16/6/26 通过SPI实现
                    MetricsFromJdbc.insertMetrics(aggregate.get(), interval, timeUnit);
                    MetricsFromElastic.insertMetrics(aggregate.get(), interval, timeUnit);
                }
            } catch (Exception e) {
                LogLog.error(ApmTimingGatherExecutor.class.getName(), e);
            }
        }
    }

}
