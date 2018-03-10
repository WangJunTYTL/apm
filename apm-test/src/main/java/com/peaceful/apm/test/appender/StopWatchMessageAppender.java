package com.peaceful.apm.test.appender;

/**
 * 将消息转发给下游Appender处理
 *
 * Created by wangjun38 on 2018/1/27.
 */
public class StopWatchMessageAppender extends DefaultMessageAppenderAttachImpl implements MessageAppenderAttach, MessageAppender {


    public void send(Object message) {
        messageLoopOnChildAppender(message);
    }
}
