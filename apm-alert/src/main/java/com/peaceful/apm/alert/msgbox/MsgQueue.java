package com.peaceful.apm.alert.msgbox;

import java.util.List;

/**
 * @author WangJun
 * @version 1.0 16/6/18
 */
public interface MsgQueue {

    public void push(Message message);

    public List<Message> pop(int size);
}
