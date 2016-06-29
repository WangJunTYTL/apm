package com.peaceful.apm.alert.msgbox;

/**
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class Message {

    public String title;
    public String content;
    public MsgLevelEnum level = MsgLevelEnum.INFO;
    public MsgSentTypeEnum sentType = MsgSentTypeEnum.EMAIL;
    public MsgMergeEnum msgMerge = MsgMergeEnum.ALL;
    public long timestamp;

    public Message() {

    }

    public Message(String title, String content) {
        this.title = title;
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String title, String content, MsgLevelEnum level) {
        this.title = title;
        this.content = content;
        this.level = level;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String title, String content, MsgSentTypeEnum sentType) {
        this.title = title;
        this.content = content;
        this.sentType = sentType;
        this.timestamp = System.currentTimeMillis();
    }

    public Message(String title, String content, MsgLevelEnum level, MsgSentTypeEnum sentType) {
        this.title = title;
        this.content = content;
        this.level = level;
        this.sentType = sentType;
        this.timestamp = System.currentTimeMillis();
    }
}
