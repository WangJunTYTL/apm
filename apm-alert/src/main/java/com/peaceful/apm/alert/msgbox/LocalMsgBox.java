package com.peaceful.apm.alert.msgbox;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 如果需要下发消息，消息会先进入到MsgBox，后台任务会定时对对消息进行合并、过滤、分发
 * <p>
 * 你也可以继承该类，实现统一的消息中心
 *
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class LocalMsgBox extends AbstractScheduledService implements MsgBox {

    @Inject
    MsgNotice msgNotice;
    private int defaultMsgBoxSize = 1024;
    BlockingQueue<Message> messages = new ArrayBlockingQueue<Message>(defaultMsgBoxSize);

    private Logger logger = LoggerFactory.getLogger(getClass());

    public LocalMsgBox() {
        this.startAsync();
    }

    @Override
    public void push(Message message) {
        boolean flag = messages.offer(message);
        if (!flag) {
            logger.warn("maybe the local msg queue is full! current size is {}", messages.size());
        }
    }

    @Override
    public List<Message> pop(int maxSize) {
        List<Message> messageList = new LinkedList<Message>();
        messages.drainTo(messageList, maxSize);
        return messageList;
    }

    @Override
    protected void runOneIteration() throws Exception {
        try {
            logger.debug("drainTo msg from local msg queue to msg box");
            List<Message> messageList = pop(defaultMsgBoxSize);
            for (Message message : messageList) {
                msgNotice.send(message);
            }
        } catch (Exception e) {
            logger.error(Throwables.getStackTraceAsString(e));
        }
    }

    @Override
    protected Scheduler scheduler() {
        //// TODO: 16/9/4 设定消息处理频率
        return Scheduler.newFixedRateSchedule(0, 30, TimeUnit.SECONDS);
    }

}


