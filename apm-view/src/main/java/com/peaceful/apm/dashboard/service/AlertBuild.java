package com.peaceful.apm.dashboard.service;

import java.util.Date;

/**
 * Created by wangjun on 16/9/12.
 */
public class AlertBuild {

    public long id;
    public String service;
    public String tag;
    public String term;
    public int interval;
    public String smsMsg;
    public String mailMsg;
    public int status;
    public String receiverGroups;
    public Date createTime;
    public Date updateTime;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getSmsMsg() {
        return smsMsg;
    }

    public void setSmsMsg(String smsMsg) {
        this.smsMsg = smsMsg;
    }

    public String getMailMsg() {
        return mailMsg;
    }

    public void setMailMsg(String mailMsg) {
        this.mailMsg = mailMsg;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getReceiverGroups() {
        return receiverGroups;
    }

    public void setReceiverGroups(String receiverGroups) {
        this.receiverGroups = receiverGroups;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
