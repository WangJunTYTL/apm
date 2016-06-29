package com.peaceful.perf4j.dashboard.config;

/**
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class Node {

    private String hostname;
    private String url;
    private String cluster;

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }
}
