package com.peaceful.apm.alert.msgbox;

import java.util.List;

/**
 * email msg format
 *
 * Created by wangjun on 16/9/4.
 */
public class MailMsg extends Message {

    public String subject;

    public MailMsg() {

    }

    public MailMsg(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

}
