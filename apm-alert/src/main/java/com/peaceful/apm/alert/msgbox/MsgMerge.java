package com.peaceful.apm.alert.msgbox;

import java.util.List;

/**
 * 消息合并逻辑
 *
 * @author WangJun
 * @version 1.0 16/6/19
 */
public interface MsgMerge {

    List<Message> merge(List<Message> messageList);
}
