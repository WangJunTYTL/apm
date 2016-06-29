package com.peaceful.apm.alert.msgbox;

import com.google.common.base.Throwables;
import com.google.inject.Inject;
import com.peaceful.apm.alert.AlertExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 如果需要下发消息，消息会先进入到MsgBox，后台任务会定时对对消息进行合并、过滤、分发
 * <p>
 * 你也可以继承该类，实现统一的消息中心
 *
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class AbstractMsgBox implements MsgQueue {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private MsgQueue localMsg;

    @Inject
    MsgNoticeHandle msgNoticeHandle;


    public AbstractMsgBox() {
        AlertExecutor.SCHEDULED_EXECUTOR_SERVICE.scheduleAtFixedRate(new MergeMsg(), 1, MsgBoxConfig.popInterval, TimeUnit.SECONDS);
        logger.info("Local MsgBox: capacity->{}", MsgBoxConfig.maxSize);
    }

    @Override
    public void push(Message message) {
        localMsg.push(message);
    }

    @Override
    public List<Message> pop(int size) {
        return localMsg.pop(size);
    }

    class MergeMsg implements Runnable {

        @Override
        public void run() {
            try {
                logger.debug("drainTo msg from local msg queue to msg box");
                List<Message> messageList = pop(MsgBoxConfig.maxSize);
                while (messageList != null && messageList.size() > 0) {
                    msgNoticeHandle.sendMessage(messageList);
                }
            } catch (Exception e) {
                logger.error(Throwables.getStackTraceAsString(e));
            }
        }
    }

}


