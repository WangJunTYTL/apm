package com.peaceful.apm.dashboard.datasource;

/**
 * Created by wangjun on 16/9/8.
 */
public class Node {

    public String service;
    public String hostname;

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
