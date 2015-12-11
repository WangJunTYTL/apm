package org.perf4j.log4j;

import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.log4j.db.sqllite.SqlLiteHelper;

import java.io.Flushable;
import java.util.Enumeration;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 15/12/10
 */
public class SqlLiteAppender extends AppenderSkeleton implements AppenderAttachable, Flushable {

    private String pathName = "/data/logs/perf4j.db";

    public String getPath() {
        return pathName;
    }

    public void setPath(String path) {
        this.pathName = path;
    }


    /**
     * Any downstream appenders are contained in this AppenderAttachableImpl
     */
    private final AppenderAttachableImpl downstreamAppenders = new AppenderAttachableImpl();

    public void addAppender(Appender appender) {
        synchronized (downstreamAppenders) {
            downstreamAppenders.addAppender(appender);
        }
    }

    public Enumeration getAllAppenders() {
        synchronized (downstreamAppenders) {
            return downstreamAppenders.getAllAppenders();
        }
    }

    public Appender getAppender(String name) {
        synchronized (downstreamAppenders) {
            return downstreamAppenders.getAppender(name);
        }
    }

    public boolean isAttached(Appender appender) {
        synchronized (downstreamAppenders) {
            return downstreamAppenders.isAttached(appender);
        }
    }

    public void removeAllAppenders() {
        synchronized (downstreamAppenders) {
            downstreamAppenders.removeAllAppenders();
        }
    }

    public void removeAppender(Appender appender) {
        synchronized (downstreamAppenders) {
            downstreamAppenders.removeAppender(appender);
        }
    }

    public void removeAppender(String name) {
        synchronized (downstreamAppenders) {
            downstreamAppenders.removeAppender(name);
        }
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        Object logMessage = loggingEvent.getMessage();
        if (logMessage instanceof GroupedTimingStatistics) {
            GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
            try {
                SqlLiteHelper.getInstance(pathName).save(statistics);
            } catch (Exception e) {
                getErrorHandler().error(e + "");
            }
        }
    }

    @Override
    public boolean requiresLayout() {
        return false;
    }

    @Override
    public void close() {

        //close any downstream appenders
        synchronized (downstreamAppenders) {
            flush();
            for (Enumeration enumer = downstreamAppenders.getAllAppenders();
                 enumer != null && enumer.hasMoreElements(); ) {
                Appender appender = (Appender) enumer.nextElement();
                appender.close();
            }
        }

    }

    public void flush() {

    }
}
