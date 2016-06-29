package com.peaceful.apm.alert.msgbox.impl;

import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.MsgBoxConfig;
import com.peaceful.apm.alert.msgbox.MsgNotice;
import com.peaceful.boot.common.helper.Console;
import com.peaceful.boot.common.helper.DateHelper;
import com.peaceful.boot.common.helper.MailHelper;

import java.util.Date;
import java.util.List;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class NopMsgNotice implements MsgNotice {

    @Override
    public void sendEmail(List<Message> messages) {
        StringBuffer buffer = new StringBuffer();
        for (Message m : messages) {
            buffer.append("<b>");
            buffer.append(m.title).append("</b>").append("&nbsp;&nbsp;&nbsp;");
            buffer.append(m.level).append("&nbsp;&nbsp;&nbsp;");
            buffer.append(DateHelper.formatDateTime(new Date(m.timestamp))).append("<br/>");
            buffer.append(m.content);
            buffer.append("<br/><br/>");
        }
        String subject = DateHelper.getStringByPattern(new Date(), "HH:mm") + "收到" + messages.size() + "条系统消息通知";
        MailHelper.send(MsgBoxConfig.userMailGroup, subject, buffer.toString());
    }

    @Override
    public void sendSms(List<Message> messages) {
        Console.log("假设这样就方式了消息");
    }
}
