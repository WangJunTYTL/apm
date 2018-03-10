package com.peaceful.apm.test.appender;

import lombok.Setter;

/**
 * 负责将消息通过http协议发送出去,
 *
 * Created by wangjun38 on 2018/1/27.
 */
public class HttpMessageAppender implements MessageAppender {


    @Setter
    private String remoteUrl; // 使用post方式去请求


    public void send(Object message) {

    }
}
