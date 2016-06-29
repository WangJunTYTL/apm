package org.perf4j.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.io.Flushable;
import java.util.SortedMap;

/**
 * @author WangJun
 * @version 1.0 16/6/20
 */
public class TimingStatisticsAppender extends AppenderSkeleton {

    protected void append(LoggingEvent event) {

        Object logMessage = event.getMessage();
        if (logMessage instanceof GroupedTimingStatistics) {
            GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
            if (statistics != null) {
                SortedMap<String, TimingStatistics> data = statistics.getStatisticsByTag();
            }
        }
    }

    public boolean requiresLayout() {
        return false;
    }

    public void close() {

    }
}
