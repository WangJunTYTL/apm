package org.perf4j.log4j;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.helpers.AppenderAttachableImpl;
import org.apache.log4j.spi.AppenderAttachable;
import org.apache.log4j.spi.LoggingEvent;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.log4j.db.sqllite.SqlLiteHelper;

import java.io.Flushable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Enumeration;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 15/12/10
 */
public class SqlLiteAppender extends AppenderSkeleton implements AppenderAttachable, Flushable {

    private String pathName = "/data/logs/perf4j.db";

    private int storageLife = 30;

    public String getPath() {
        return pathName;
    }

    public void setPath(String path) {
        this.pathName = path;
    }

    public void setStorage(int storage) {
        this.storageLife = storage;
    }

    public int getStorage() {
        return storageLife;
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

    private static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    private class DeleteHistoryData implements Runnable {

        public void run() {

            Date date = DateUtils.addDays(new Date(), (-storageLife));
            SqlLiteHelper sqlLiteHelper = SqlLiteHelper.getInstance();
            if (sqlLiteHelper != null) {
                try {
                    sqlLiteHelper.deleteData(new java.sql.Date(date.getTime()));
                } catch (SQLException e) {
                    getErrorHandler().error("clear history data error "+ e);
                }
            }
        }
    }

    @Override
    public synchronized void activateOptions() {
        System.out.println("clear-history-data-" + storageLife + " start");
        scheduledExecutorService.scheduleAtFixedRate(new DeleteHistoryData(), 0, 1, TimeUnit.DAYS);
    }
}
