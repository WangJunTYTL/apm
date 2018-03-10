package com.peaceful.apm.test.appender;

/**
 * 定义消息输出
 *
 * Created by wangjun38 on 2018/1/27.
 */
public interface MessageAppender {

    /**
     * 将消息发送到指定目的地
     */
    void send(Object message);

}
