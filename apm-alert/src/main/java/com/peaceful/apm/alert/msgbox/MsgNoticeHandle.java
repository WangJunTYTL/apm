package com.peaceful.apm.alert.msgbox;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class MsgNoticeHandle {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    private MsgNotice msgNotice;

    public void sendMessage(List<Message> messageList) {
        List<Message> toSms = Lists.newArrayList();
        List<Message> toEmail = Lists.newArrayList();
        if (messageList != null && messageList.size() != 0) {
            for (Message message : messageList) {
                if (message.sentType == MsgSentTypeEnum.EMAIL) {
                    toEmail.add(message);
                } else if (message.sentType == MsgSentTypeEnum.SMS) {
                    toSms.add(message);
                } else {
                    toEmail.add(message);
                    toSms.add(message);
                }
            }
        }
        if (toEmail != null && toEmail.size() != 0) {
            msgNotice.sendEmail(toEmail);
        }
        if (toSms != null && toSms.size() != 0) {
            if (toSms.size() < 8) {
                msgNotice.sendSms(toSms);
            }else {
                logger.warn("too much sms，email instead");
                Message message = new Message("注意:太多的消息","消息下发方式切换到邮件，系统发现太多的短息消息，已自动改为使用邮件下发且只" +
                        "将前8条消息使用短信下发", MsgLevelEnum.WARN);
                toSms.add(message);
                msgNotice.sendEmail(toSms);
            }
        }
    }


}
