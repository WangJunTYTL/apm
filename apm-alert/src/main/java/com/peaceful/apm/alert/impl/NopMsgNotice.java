package com.peaceful.apm.alert.impl;

import com.peaceful.apm.alert.msgbox.*;
import com.peaceful.boot.common.helper.Console;
import com.peaceful.boot.common.helper.DateHelper;
import com.peaceful.boot.common.helper.MailHelper;

import java.util.Date;
import java.util.List;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class NopMsgNotice implements MsgHandler {

    @Override
    public void sendEmail(MailMsg msg) {
        MailHelper.send(msg.receivers, msg.subject, msg.content);
    }

    @Override
    public void sendSms(SmsMsg msg) {
        Console.log("假设这样就方式了消息");
    }
}
