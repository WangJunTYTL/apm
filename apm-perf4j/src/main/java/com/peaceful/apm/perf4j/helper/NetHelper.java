package com.peaceful.apm.perf4j.helper;

import com.google.common.base.Throwables;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wangjun on 16/1/18.
 */
public class NetHelper {


    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            Throwables.propagate(e);
        }
        return "NULL";
    }


    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            Throwables.propagate(e);
        }
        return "NULL";
    }

    public static boolean ping(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            Throwables.propagate(e);
            return false;
        }
    }
}
