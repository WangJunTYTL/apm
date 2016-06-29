package org.perf4j.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

/**
 * @author WangJun
 * @version 1.0 16/6/7
 */
public class MyAppenderTest extends AppenderSkeleton {


    protected void append(LoggingEvent event) {
        System.out.println("Test->"+event.getLevel()+event.getMessage());
    }

    public boolean requiresLayout() {
        return false;
    }

    public void close() {
        this.closed = true;
    }
}
