package com.peaceful.apm.alert.msgbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 消息会定时推到CloudQueue
 *
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class LocalMsgQueue implements MsgQueue {

    BlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(MsgBoxConfig.maxSize);

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void push(Message message) {
        boolean flag = messages.offer(message);
        if (!flag) {
            logger.warn("maybe the local msg queue is full! current size is {}", messages.size());
        }
    }

    @Override
    public List<Message> pop(int maxSize) {
        List<Message> messageList = new LinkedList<>();
        messages.drainTo(messageList, maxSize);
        messages.clear();
        return messageList;
    }

}
