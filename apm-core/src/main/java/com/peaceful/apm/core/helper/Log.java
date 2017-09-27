package com.peaceful.apm.core.helper;

import com.google.common.base.Throwables;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by wangjun38 on 2017/9/17.
 */
public class Log {


    private static String logPrefix = "apm.console.log";

    private static boolean isDebug() {
//        return true;
        return false;
    }

    public static void debug(String msg) {
        if (isDebug()) {
            print(Level.DEBUG, msg);
        }
    }

    public static void debug(Throwable e) {
        if (isDebug()) {
            if (e.getMessage() != null) {
                print(Level.DEBUG, e.getMessage());
            } else {
                print(Level.DEBUG, Throwables.getStackTraceAsString(e));
            }
        }
    }

    public static void info(String msg) {
        print(Level.INFO, msg);
    }

    public static void warn(String msg) {
        print(Level.WARN, msg);
    }

    public static void warn(Throwable e) {
        if (e.getMessage() != null) {
            print(Level.WARN, e.getMessage());
        } else {
            print(Level.WARN, Throwables.getStackTraceAsString(e));
        }
    }

    public static void error(String msg) {
        print(Level.ERROR, msg);
    }
    public static void error(Exception e) {
        if (e.getMessage() != null) {
            print(Level.ERROR, e.getMessage());
        } else {
            print(Level.ERROR, Throwables.getStackTraceAsString(e));
        }
    }


    private static void print(Level level, String msg) {
        Logger logger = LoggerFactory.getLogger(logPrefix);
        if (level == Level.DEBUG && logger.isDebugEnabled()) {
            logger.debug(msg);
        } else if (level == Level.INFO && logger.isInfoEnabled()) {
            logger.info(msg);
        } else if (level == Level.WARN && logger.isWarnEnabled()) {
            logger.warn(msg);
        } else if (level == Level.ERROR && logger.isErrorEnabled()) {
            logger.error(msg);
        } else {
            System.out.println(DateHelper.formatDateTime(new Date()) + " [" + level.name() + "] [" + logPrefix + "] " + msg);
        }
    }

    enum Level {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

}
