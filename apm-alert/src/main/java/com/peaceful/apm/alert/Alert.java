package com.peaceful.apm.alert;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.MsgBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    }

    /**
     * 消息通知
     * 注意：下发的消息也许不会立即进行发送，它会先放入到本地的MsgBox，然后定时的批量处理，比如合并、过滤、分发下发处理
     */
    public static void notice(Message message) {
        INJECTOR.getInstance(MsgBox.class).push(message);
    }

    /**
     * 创建一个新的监控规则
     *
     * @param tag 该规则的标识，同一个tag标识的规则只允许被创建一次
     */
    public static AlertBuild newItem(String tag) {
        return new AlertBuild(tag,INJECTOR.getInstance(TermService.class));
    }






}
