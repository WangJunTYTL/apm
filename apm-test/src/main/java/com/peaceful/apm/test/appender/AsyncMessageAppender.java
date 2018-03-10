package com.peaceful.apm.test.appender;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 负责将数据异步转发到下游Appender
 *
 * 说明
 * * 如果下游appender抛出异常给此类，此类不会对任何处理，直接忽略掉任何异常的信息
 *
 * Created by wangjun38 on 2018/1/29.
 */
public class AsyncMessageAppender extends DefaultMessageAppenderAttachImpl implements MessageAppender {

    private BlockingQueue<Object> messageBuffer = new ArrayBlockingQueue<Object>(1024);
    private static long threadSeqNumber;

    private static synchronized long nextThreadID() {
        return ++threadSeqNumber;
    }

    public AsyncMessageAppender() {
        Dispatch dispatch = new Dispatch();
        dispatch.setName("apm-core-async-appender-" + nextThreadID());
        dispatch.setDaemon(true);
        dispatch.start();
    }

    public void send(Object message) {
        if (!messageBuffer.offer(message)) {
            System.err.println("message discard because of buffer is full!");
        }
    }

    private class Dispatch extends Thread {

        @Override
        public void run() {
            while (true) {
                try {
                    Object message = messageBuffer.take();
                    messageLoopOnChildAppender(message); // 向下游转发
                } catch (Exception e) {
                    // ignore  异常需要在各自的appender内处理，这里不做任何异常处理
                }
            }
        }
    }
}
