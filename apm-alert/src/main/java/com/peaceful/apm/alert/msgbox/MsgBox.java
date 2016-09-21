package com.peaceful.apm.alert.msgbox;

import java.util.List;

/**
 * 消息盒子  这样命名的缘由是:发送消息可以集中处理,比如要做到分组发送、邮件短信发送、高级点的可以做到消息合并后发送、做到分布式消息处理
 *
 * @author WangJun
 * @version 1.0 16/6/18
 */
public interface MsgBox {

    /**
     * 发送消息到消息盒
     *
     * @param message
     */
    void push(Message message);

    /**
     * 从消息盒中获取size条消息
     *
     * @param size
     * @return
     */
    List<Message> pop(int size);
}
