package com.peaceful.apm.alert.msgbox;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wangjun on 16/9/4.
 */
public class MsgNotice {

    @Inject
    MsgHandler msgHandler;

    public void send(Message message) {
        Preconditions.checkArgument(StringUtils.isNoneBlank(message.content), "msg content is empty!");
        Preconditions.checkArgument(StringUtils.isNoneBlank(message.receivers), "msg receiver is empty!");
        // email 消息
        if (message instanceof MailMsg) {
            MailMsg mailMsg = (MailMsg) message;
            msgHandler.sendEmail(mailMsg);
        }
        // sms 消息
        else if (message instanceof SmsMsg) {
            SmsMsg smsMsg = (SmsMsg) message;
            msgHandler.sendSms(smsMsg);
        }
    }
}
