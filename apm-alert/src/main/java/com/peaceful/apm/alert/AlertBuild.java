package com.peaceful.apm.alert;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.peaceful.apm.alert.msgbox.MailMsg;
import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.SmsMsg;
import com.peaceful.apm.alert.users.User;
import com.peaceful.boot.Application;
import com.peaceful.boot.common.helper.DateHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * build a alert instance
 * <p>
 * Created by wangjun on 16/9/13.
 */
public class AlertBuild {


    private String tag; // 唯一规则名称
    private String term; // 触发规则表达式
    private String smsMsg; // 短信消息内容
    private String emailMsg; // 邮件消息内容 邮件内容支持html格式
    private List<User> receivers; // 接收消息用户组
    private long interval; // 执行调度平率，单位分钟
    private MetricCallback metricCallback; // 监测指标

    private TermService termService;

    private final static Logger LOGGER = LoggerFactory.getLogger(AlertBuild.class);

    public AlertBuild(String tag, TermService termService) {
        this.tag = tag;
        this.termService = termService;
    }

    /**
     * 监控数据指标
     *
     * @param metricCallback
     */
    public AlertBuild metric(MetricCallback metricCallback) {
        this.metricCallback = metricCallback;
        return this;
    }

    /**
     * 预警规则：如果有多个可以用&符号相连，如：${api.error.count}>100&${api.mean}>1000
     *
     * @param term 预警规则表达式
     */
    public AlertBuild term(String term) {
        this.term = term;
        return this;
    }

    /**
     * 触发预警时下发消息内容
     *
     * @param messages
     */
    public AlertBuild notice(Message... messages) {
        for (Message msg : messages) {
            if (msg instanceof SmsMsg) {
                this.smsMsg = JSON.toJSONString(smsMsg);
            } else if (msg instanceof MailMsg) {
                this.emailMsg = JSON.toJSONString(msg);
            }
        }
        return this;
    }

    /**
     * 消息接收用户组
     */
    public AlertBuild to(List<User> receivers) {
        this.receivers = receivers;
        return this;
    }

    /**
     * 预警规则距离下次执行时间间隔，一般会在1分钟以上，建议五分钟一次
     *
     * @param interval
     */
    public AlertBuild interval(long interval) {
        Preconditions.checkArgument(interval > 0, "interval must > 0s");
        this.interval = interval;
        return this;
    }

    /**
     * 校验AlertBuild的正确性，如果没问题就启动该规则
     */
    public void build() {
        Preconditions.checkArgument(StringUtils.isNoneBlank(tag), "tag is blank");
        Preconditions.checkArgument(StringUtils.isNoneBlank(term), "term is blank");
        Preconditions.checkArgument((StringUtils.isNotBlank(smsMsg) || StringUtils.isNotBlank(emailMsg)), "notice message  is null");
        Preconditions.checkArgument((receivers != null && !receivers.isEmpty()), "message receiver groups  is null");
        Preconditions.checkArgument(interval > 0, "interval must gt 0");
        Preconditions.checkArgument(metricCallback != null, "metricCallback  is null");

        Preconditions.checkArgument(!AlertProcess.processState.containsKey(tag), "tag is repeat");
        AlertProcess alertProcess = new AlertProcess();
        alertProcess.state = AlertProcess.State.NEW;
        AlertProcess.processState.put(tag, alertProcess);
        AlertExecutor.SCHEDULED_EXECUTOR_SERVICE.scheduleWithFixedDelay(new AlertTask(this), 0, interval, TimeUnit.MINUTES);
        LOGGER.info("Alert Build Event:tag:{}, term:{}, interval:{}min", tag, term, interval);
    }

    private class AlertTask implements Runnable {

        private AlertBuild alertBuild;

        public AlertTask(AlertBuild alertBuild) {
            this.alertBuild = alertBuild;
        }

        @Override
        public void run() {
            try {
                AlertProcess alertProcess = AlertProcess.processState.get(alertBuild.tag);
                if (alertProcess.state.equals(AlertProcess.State.NEW)) {
                    alertProcess.state = AlertProcess.State.START;
                } else if (alertProcess.state.equals(AlertProcess.State.WAIT_TERMINATED)) {
                    alertProcess.state = AlertProcess.State.TERMINATED;
                    AlertProcess.processState.remove(alertBuild.tag);
                    LOGGER.info("Alert Stop Event:tag:{}, term:{}", tag, term);
                    throw new NeedTerminatedException();
                } else if (alertProcess.state.equals(AlertProcess.State.START)) {
                    if ((System.currentTimeMillis() - alertProcess.nextProcessTime) > 5 * 60 * 1000) {
                        StringBuffer buffer = new StringBuffer();
                        buffer.append("<b>tag:</b>").append(alertBuild.tag).append("<br>");
                        buffer.append("<b>term:</b>").append(alertBuild.term).append("<br>");
                        buffer.append("<b>plan.dispatch.time:</b>").append(DateHelper.formatDateTime(new Date(alertProcess.nextProcessTime))).append("<br>");
                        buffer.append("<b>reality.dispatch.time:</b>").append(DateHelper.formatDateTime(new Date())).append("<br>");
                        MailMsg message = new MailMsg("Alert Dispatch Event：dispatch delay warn", buffer.toString());
                        message.receivers = Application.getConfigContext().getString("apm.admin.email");
                        Alert.notice(message);
                    }
                } else if (alertProcess.state.equals(AlertProcess.State.TERMINATED)) {
                    LOGGER.error("Alert Dispatch Event:Failure to Terminated tag:{}, term:{}", tag, term);
                    throw new NeedTerminatedException();
                }
                // calculation term expression
                Map<String, Object> kvs = alertBuild.metricCallback.get();
                LOGGER.debug("execute alert rule {}:{}", alertBuild.tag, alertBuild.term);
                if (termService.isTrue(alertBuild.term, kvs)) {
                    LOGGER.info("Alert Trigger Event:{},term:{}", alertBuild.tag, alertBuild.term);
                    if (StringUtils.isNotBlank(alertBuild.smsMsg)) {
                        Alert.notice(JSON.parseObject(termService.frame(alertBuild.smsMsg, kvs), SmsMsg.class));
                    }
                    if (StringUtils.isNotBlank(alertBuild.emailMsg))
                        Alert.notice(JSON.parseObject(termService.frame(alertBuild.emailMsg, kvs), MailMsg.class));
                }

                alertProcess.nextProcessTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(alertBuild.interval, TimeUnit.MINUTES);
                LOGGER.info("Alert Dispatch Event:{} next process time:{}", alertBuild.tag, DateHelper.formatDateTime(new Date(alertProcess.nextProcessTime)));
            } catch (NeedTerminatedException e) {
                // thread will shutdown and the alert task will shutdown
                throw new NeedTerminatedException();
            } catch (Exception e) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("<b>tag:</b>").append(alertBuild.tag).append("<br>");
                buffer.append("<b>term expression:</b>").append(alertBuild.term).append("<br>");
                buffer.append("<b>sms msg:</b>").append(alertBuild.smsMsg).append("<br>");
                buffer.append("<b>email msg:</b>").append(alertBuild.emailMsg).append("<br>");
                buffer.append("<b>cause:</b>").append(Throwables.getStackTraceAsString(e)).append("<br>");
                MailMsg message = new MailMsg("Alert Dispatch Event: execute exception", buffer.toString());
                message.receivers = Application.getConfigContext().getString("apm.admin.email");
                Alert.notice(message);
                LOGGER.error("Alert Dispatch Event: execute exception,tag:{},term:{}, cause:{}", alertBuild.tag, alertBuild.term, Throwables.getStackTraceAsString(e));
            }
        }

    }
}
