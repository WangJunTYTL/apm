package org.perf4j.db.sqlite;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by wangjun on 16/1/18.
 */
public class NetHelper {

    static Logger logger = LoggerFactory.getLogger(NetHelper.class);

    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("error: ", e);
        }
        return "Empty";
    }


    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            logger.error("error: ", e);
        }
        return "Empty";
    }

    public static boolean ping(String ip, int seconds) {
        int timeOut = seconds;
        try {
            return InetAddress.getByName(ip).isReachable(timeOut);
        } catch (IOException e) {
            return false;
        }
    }
}
