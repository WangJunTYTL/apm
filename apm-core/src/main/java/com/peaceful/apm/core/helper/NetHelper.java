package com.peaceful.apm.core.helper;

import com.google.common.base.Throwables;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by wangjun on 16/1/18.
 */
public class NetHelper {


    public static String getIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            // .ignore
        }
        return "Unknown";
    }


    public static String getHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            // .ignore
        }
        return "Unknown";
    }

    public static boolean ping(String ip, int timeout) {
        try {
            return InetAddress.getByName(ip).isReachable(timeout);
        } catch (IOException e) {
            Throwables.propagate(e);
            return false;
        }
    }

    public static boolean pingIpPort(String host, int port, int time) {

        if (time <= 0) time = 1;
        for (int i = 0; i < time; i++) {
            Socket socket = new Socket();
            try {
                socket.connect(new InetSocketAddress(host, port));
                return true;
            } catch (IOException e) {
                // .ignore
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    // .ignore
                }
            }
        }
        return false;
    }
}
