package com.peaceful.apm.alert;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.peaceful.apm.alert.el.TermQuery;
import com.peaceful.apm.alert.el.Var;
import com.peaceful.apm.alert.msgbox.AbstractMsgBox;
import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.MsgLevelEnum;
import com.peaceful.apm.alert.msgbox.MsgQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 预警规则定义，你可以自定义变量并可以支持通过表达式定义触发规则，具体查看方法{@link #newItem(String)}
 *
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class Alert {

    private static final Logger LOGGER = LoggerFactory.getLogger(Alert.class);

    protected static final Injector INJECTOR;

    static {
        /**
         * Guice.createInjector() takes your Modules, and returns a new Alert
         * instance. Most applications will call this method exactly once, in their
         * main() method.
         */
        INJECTOR = Guice.createInjector(new AlertModule());
        // 启动本地MsgBox
        INJECTOR.getInstance(AbstractMsgBox.class);

    }

    /**
     * 消息通知
     * 注意：下发的消息也许不会立即进行发送，它会先放入到本地的MsgBox，然后定时的批量处理，比如合并、过滤、分发下发处理
     */
    public static void notice(Message message) {
        INJECTOR.getInstance(MsgQueue.class).push(message);
    }

    /**
     * 创建一个新的监控规则
     *
     * @param tag 该规则的标识，同一个tag标识的规则只允许被创建一次
     */
    public static AlertBuild newItem(String tag) {
        return new AlertBuild(tag);
    }


    static class AlertBuild {

        private String tag;
        private String term;
        private Message message;
        private String receivers;
        private long interval;
        private MetricCallback metricCallback;

        public AlertBuild(String tag) {
            this.tag = tag;
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
         * @param message
         */
        public AlertBuild notice(Message message) {
            this.message = message;
            return this;
        }

        /**
         * 触发预警时消息接收者
         *
         * @param receivers
         */
        public AlertBuild to(String receivers) {
            this.receivers = receivers;
            return this;
        }

        /**
         * 监控数据来源
         *
         * @param metricCallback
         */
        public AlertBuild metric(MetricCallback metricCallback) {
            this.metricCallback = metricCallback;
            return this;
        }

        /**
         * 预警规则距离下次执行时间间隔，一般会在1分钟以上，建议五分钟一次
         *
         * @param interval
         * @param timeUnit
         */
        public AlertBuild interval(long interval, TimeUnit timeUnit) {
            this.interval = TimeUnit.SECONDS.convert(interval, timeUnit);
            Preconditions.checkArgument(interval != 0, "interval must > 0s");
            return this;
        }

        /**
         * 校验AlertBuild的正确性，如果没问题就启动该规则
         */
        public void build() {
            Preconditions.checkArgument(StringUtils.isNoneBlank(tag), "tag is blank");
            Preconditions.checkArgument(StringUtils.isNoneBlank(term), "term is blank");
            Preconditions.checkArgument(message != null, "notice message  is null");
            Preconditions.checkArgument(receivers != null, "receivers  is null");
            Preconditions.checkArgument(interval != 0, "interval can't equals 0");
            Preconditions.checkArgument(metricCallback != null, "metricCallback  is null");
            AlertExecutor.SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new AlertTask(this), interval, interval, TimeUnit.SECONDS);
            LOGGER.info("add new alert rule->{} term->({}) interval->{}s success", tag, term, interval);
        }

        private static class AlertTask implements Runnable {

            private static final TermQuery TERM_QUERY = new TermQuery();
            private AlertBuild alertBuild;

            public AlertTask(AlertBuild alertBuild) {
                this.alertBuild = alertBuild;
            }

            @Override
            public void run() {
                try {
                    Map<String, String> kvs = alertBuild.metricCallback.get();
                    if (TERM_QUERY.bool(alertBuild.term, kvs)) {
                        LOGGER.debug("execute alert rule {}:{}", alertBuild.tag, alertBuild.term);
                        kvs.put("tag", alertBuild.tag);
                        kvs.put("term", alertBuild.term);
                        if (alertBuild.interval >= 60 && alertBuild.interval < 3600) {
                            kvs.put("interval", String.valueOf(TimeUnit.MINUTES.convert(alertBuild.interval, TimeUnit.SECONDS)) + "min");
                        } else if (alertBuild.interval < 60) {
                            kvs.put("interval", String.valueOf(alertBuild.interval) + "s");
                        } else {
                            kvs.put("interval", String.valueOf(TimeUnit.HOURS.convert(alertBuild.interval, TimeUnit.SECONDS)) + "min");
                        }
                        Message message = alertBuild.message;
                        message.title = Var.getContent(message.title, kvs);
                        message.content = Var.getContent(message.content, kvs);
                        LOGGER.debug("trigger {} rule,term->{}", alertBuild.tag,alertBuild.term);
                        Alert.notice(message);
                    }
                } catch (Exception e) {
                    // 服务本身错误了,发送邮件通知
                    Message message = new Message("alert rule execute error", Throwables.getStackTraceAsString(e), MsgLevelEnum.ERROR);
                    Alert.notice(message);
                    LOGGER.error("alert rule {}->{} error:{}", alertBuild.tag, alertBuild.term, Throwables.getStackTraceAsString(e));
                }
            }
        }
    }


}
