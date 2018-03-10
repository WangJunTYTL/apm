package com.peaceful.apm.test.appender;

import java.util.List;

/**
 * Created by wangjun38 on 2018/1/27.
 */
public interface MessageAppenderAttach {


    List<MessageAppender> getChildChannelList();

    void addMessageAppender(MessageAppender messageChannel);


}
