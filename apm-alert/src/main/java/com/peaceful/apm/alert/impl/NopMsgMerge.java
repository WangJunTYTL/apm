package com.peaceful.apm.alert.impl;

import com.peaceful.apm.alert.msgbox.Message;
import com.peaceful.apm.alert.msgbox.MsgMerge;

import java.util.List;

/**
 * @author WangJun
 * @version 1.0 16/6/19
 */
public class NopMsgMerge implements MsgMerge {

    @Override
    public List<Message> merge(List<Message> messageList) {
        return messageList;
    }
}
