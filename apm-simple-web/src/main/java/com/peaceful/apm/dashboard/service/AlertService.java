package com.peaceful.apm.dashboard.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.AbstractScheduledService;

import com.peaceful.apm.alert.Alert;
import com.peaceful.apm.alert.AlertProcess;
import com.peaceful.apm.alert.MetricCallback;
import com.peaceful.apm.alert.helper.DateHelper;
import com.peaceful.apm.alert.msgbox.MailMsg;
import com.peaceful.apm.alert.msgbox.SmsMsg;
import com.peaceful.apm.alert.users.User;
import com.peaceful.apm.dashboard.datasource.DataSourceDao;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun on 16/9/13.
 */
@Service
public class AlertService extends AbstractScheduledService {

    private Map<String, AlertBuild> alertBuildMap = new HashMap<>();

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    @Autowired
    private UserService userService;

    private DecimalFormat df = new DecimalFormat("0.00");

    private Logger logger = LoggerFactory.getLogger(getClass());

    public AlertService() {
        this.startAsync();
    }

    @Override
    protected void runOneIteration() throws Exception {
        try {
            List<String> services = dataSourceService.selectAllService();
            for (String service : services) {
                List<AlertBuild> alertBuildList = dataSourceService.selectAllAlert(service);
                for (AlertBuild alertBuild : alertBuildList) {
                    String key = "[" + alertBuild.getService() + "][" + alertBuild.getTag() + "]";
                    if (alertBuild.status == 0) {
                        if (alertBuildMap.containsKey(key)) {
                            AlertBuild alertBuildOld = alertBuildMap.get(key);
                            // update as version of alert instance,if update is equals,don't load
                            if (alertBuild.updateTime.equals(alertBuildOld.updateTime)) {
                                continue;
                            } else {
                                // need terminated when change
                                if (AlertProcess.getState(key).equals(AlertProcess.State.START)) {
                                    terminatedAlert(key);
                                    continue;
                                } else {
                                    terminatedAlert(key);
                                    throw new IllegalMonitorStateException("alert state should is start");
                                }
                            }
                        }
                        // alert state must is terminated before load
                        if (!AlertProcess.getState(key).equals(AlertProcess.State.TERMINATED)) {
                            continue;
                        }
                        StopWatch stopWatch = new Slf4JStopWatch();

                        MailMsg mailMsg = null;
                        SmsMsg smsMsg = null;
                        if (StringUtils.isNoneBlank(alertBuild.getMailMsg())) {
                            mailMsg = JSON.parseObject(alertBuild.getMailMsg(), MailMsg.class);
                        }
                        if (StringUtils.isNoneBlank(alertBuild.getSmsMsg())) {
                            smsMsg = JSON.parseObject(alertBuild.getSmsMsg(), SmsMsg.class);
                        }
                        // TODO: 16/9/13  users
                        JSONArray ids = JSON.parseArray(alertBuild.receiverGroups);
                        List<Long> idsC = new ArrayList();
                        for (Object id : ids) {
                            idsC.add(Long.valueOf(id.toString()));
                        }
                        List<User> users = userService.selectUsersByGroupId(idsC);
                        StringBuffer phones = new StringBuffer();
                        StringBuffer mails = new StringBuffer();
                        for (User user : users) {
                            if (StringUtils.isNotBlank(user.phone)) {
                                phones.append(user.phone).append(",");
                            }
                            if (StringUtils.isNotBlank(user.email)) {
                                mails.append(user.email).append(",");
                            }
                        }
                        if (mailMsg != null) {
                            mailMsg.receivers = mails.substring(0, mails.length() - 1);
                        }
                        if (smsMsg != null) {
                            smsMsg.receivers = phones.substring(0, phones.length() - 1);
                        }

                        // build alert
                        Alert.newItem(key).term(alertBuild.term).interval(alertBuild.interval).notice(smsMsg, mailMsg).to(users).metric(new MetricCallBack(alertBuild)).build();
                        alertBuildMap.put(key, alertBuild);
                        stopWatch.stop("alert.build");

                    } else {
                        if (alertBuildMap.containsKey(key)) {
                            if (AlertProcess.getState(key).equals(AlertProcess.State.START)) {
                                terminatedAlert(key);
                            }
                        } else {
                            // nothing to do
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("alert service status:{} ,cause:{}", state(), Throwables.getStackTraceAsString(e));
        }

    }

    private void terminatedAlert(String key) {
        StopWatch stopWatch = new Slf4JStopWatch();
        alertBuildMap.remove(key);
        AlertProcess.setTerminated(key);
        stopWatch.stop("alert.terminated");
    }

    private class MetricCallBack implements MetricCallback {

        private AlertBuild build;

        public MetricCallBack(AlertBuild build) {
            this.build = build;
        }

        @Override
        public Map<String, Object> get() {
            StopWatch stopWatch = new Slf4JStopWatch();
            SqlSession sqlSession = sqlSessionFactory.openSession();
            try {
                Map kvs = sqlSession.getMapper(DataSourceDao.class).selectAvgData(dataSourceService.getRouteTableId(build.service), build.tag, System.currentTimeMillis() - TimeUnit.MILLISECONDS.convert(build.interval, TimeUnit.MINUTES));
                if (kvs == null || kvs.isEmpty()) {
                    if (kvs == null) kvs = Maps.newHashMap();
                    kvs.put("count", 0);
                    kvs.put("mean", 0);
                    kvs.put("max", 0);
                    kvs.put("min", 0);
                    kvs.put("interval", "1");
                }
                int interval = Integer.valueOf(kvs.get("interval").toString());
                double count = Double.valueOf(kvs.get("value").toString());
                kvs.put("service", build.service); // service
                kvs.put("tag", build.tag); // metric
                kvs.put("tps", df.format(count / TimeUnit.SECONDS.convert(build.getInterval(), TimeUnit.MINUTES))); // tps
                kvs.put("interval", build.interval);
                kvs.put("datetime", DateHelper.formatDateTime(new Date())); // alert dataTime
                return kvs;
            } finally {
                stopWatch.stop("alert.metric");
                sqlSession.close();
            }
        }
    }

    @Override
    protected Scheduler scheduler() {
        logger.info("alert service will start in one minutes");
        return Scheduler.newFixedDelaySchedule(1, 1, TimeUnit.MINUTES);
    }
}
