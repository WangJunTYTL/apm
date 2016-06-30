package com.peaceful.apm.aggregate;

import com.google.common.base.Throwables;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.perf4j.GroupedTimingStatistics;

import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/30
 */
public class MyAppender extends AppenderSkeleton {

    @Override
    protected void append(LoggingEvent loggingEvent) {
        Object logMessage = loggingEvent.getMessage();
        if (logMessage instanceof GroupedTimingStatistics) {
            GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
            try {
                // 保存数据
            } catch (Exception e) {
                getErrorHandler().error(Throwables.getStackTraceAsString(e));
            }
        }
    }

    @Override
    public void close() {

    }

    @Override
    public boolean requiresLayout() {
        return false;
    }
}
