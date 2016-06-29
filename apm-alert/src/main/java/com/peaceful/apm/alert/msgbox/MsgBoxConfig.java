package com.peaceful.apm.alert.msgbox;

import com.peaceful.boot.Application;

import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class MsgBoxConfig {

    public static String applicationName;
    public static String userMailGroup;
    public static String userPhoneGroup;
    public static long popInterval = 5; // min
    public static int maxSize = 256;
    public static String msgNoticeImpl = "com.peaceful.boot.msg.impl.NopMsgNotice";

    static {
        applicationName = Application.getConfigContext().getString("name");
        userMailGroup = Application.getConfigContext().getString("apm.msg.mail.group");
        userPhoneGroup = Application.getConfigContext().getString("apm.msg.phone.group");
        popInterval = Application.getConfigContext().getDuration("apm.msg.pop.interval", TimeUnit.SECONDS);
        msgNoticeImpl = Application.getConfigContext().getString("apm.msg.notice.impl");
        maxSize = Application.getConfigContext().getInt("apm.msg.box.max.size");
    }
}
