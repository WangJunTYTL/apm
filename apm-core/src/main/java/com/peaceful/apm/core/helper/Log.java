package com.peaceful.apm.core.helper;

import com.google.common.base.Throwables;

import java.util.Date;

/**
 * Created by wangjun38 on 2017/9/17.
 */
public class Log {


    private static String logPrefix = "apm_console_log";

    private static boolean isDebug() {
        return true;
//        return false;
    }

    public static void debug(String msg) {
        if (isDebug()) {
            print(msg);
        }
    }

    public static void info(String msg) {
        print(msg);
    }

    public static void warn(String msg) {
        print(msg);
    }

    public static void warn(Throwable e) {
        if (e.getMessage() != null) {
            print(e.getMessage());
        } else {
            print(Throwables.getStackTraceAsString(e));
        }
    }

    public static void debug(Throwable e) {
        if (isDebug()) {
            if (e.getMessage() != null) {
                print(e.getMessage());
            } else {
                print(Throwables.getStackTraceAsString(e));
            }
        }
    }

    private static void print(String msg) {
        System.out.println(DateHelper.formatDateTime(new Date()) + " ["+logPrefix+"] " + msg);
    }

}
