package com.peaceful.perf4j.dashboard.config;

/**
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class Graph {

    private GraphEnum type;
    private String title;
    private String subTitle;
    private String[] tags;
    private String[] clusters;
    private String page;


    public GraphEnum getType() {
        return type;
    }

    public void setType(GraphEnum type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String[] getClusters() {
        return clusters;
    }

    public void setClusters(String[] clusters) {
        this.clusters = clusters;
    }
}
