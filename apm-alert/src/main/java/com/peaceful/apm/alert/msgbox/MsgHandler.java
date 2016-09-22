package com.peaceful.apm.alert.msgbox;

import java.util.List;

/**
 * 消息通知形式
 *
 * @author WangJun
 * @version 1.0 16/6/19
 */
public interface MsgHandler {

    void sendEmail(MailMsg msg);

    void sendSms(SmsMsg msg);
}
