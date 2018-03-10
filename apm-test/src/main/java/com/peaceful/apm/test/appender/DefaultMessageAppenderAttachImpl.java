package com.peaceful.apm.test.appender;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 关联多个appender，维护appender间的继承关系
 *
 * Created by wangjun38 on 2018/1/27.
 */
public class DefaultMessageAppenderAttachImpl implements MessageAppenderAttach {

    private List<MessageAppender> childMessageChannelList = new CopyOnWriteArrayList<MessageAppender>();

    public List<MessageAppender> getChildChannelList() {
        return childMessageChannelList;
    }

    public void addMessageAppender(MessageAppender messageChannel) {
        childMessageChannelList.add(messageChannel);
    }

    public void messageLoopOnChildAppender(Object message) {
        for (MessageAppender messageChannel : childMessageChannelList) {
            messageChannel.send(message); // 如果此时有异常抛出，后面的appender不会受到消息
        }
    }
}
