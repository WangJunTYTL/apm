package com.peaceful.apm.test.appender;

/**
 * 把消息直接输出到控制台
 *
 * Created by wangjun38 on 2018/1/27.
 */
public class ConsoleMessageAppender implements MessageAppender {


    public void send(Object message) {
        System.out.println(message);
    }
}
