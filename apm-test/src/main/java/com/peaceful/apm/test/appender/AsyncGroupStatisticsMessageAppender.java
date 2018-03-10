package com.peaceful.apm.test.appender;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import com.codahale.metrics.Histogram;
import com.codahale.metrics.Snapshot;
import com.peaceful.apm.test.message.StopWatchMessageEvent;
import com.peaceful.apm.test.message.StopWatchMetric;
import com.peaceful.apm.test.metric.MetricsComponent;
import com.peaceful.apm.test.metric.StopWatchParse;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun38 on 2018/1/27.
 */
public class AsyncGroupStatisticsMessageAppender extends DefaultMessageAppenderAttachImpl implements MessageAppender {

    private ArrayBlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(1024);
    private LinkedList<String> drainMessageQueue = new LinkedList<String>();
    StopWatchParse stopWatchParse = new StopWatchParse();
    private long timeSplice = 60;
    private static final long NANOS_IN_A_MILLI = 1000000L;
    private static long threadSeqNumber;

    private static synchronized long nextThreadID() {
        return ++threadSeqNumber;
    }

    public AsyncGroupStatisticsMessageAppender() {
        Dispatch dispatch = new Dispatch();
        dispatch.setName("apm-core-async-group-statistics-" + nextThreadID());
        dispatch.start();
    }

    public AsyncGroupStatisticsMessageAppender(long timeSplice) {
        this.timeSplice = timeSplice;
        Dispatch dispatch = new Dispatch();
        dispatch.setName("apm-core-async-group-statistics-" + nextThreadID());
        dispatch.start();
    }

    public void send(Object message) {
        if (messageQueue.offer((String) message)) {
            // ignore
        } else {
            // todo
            System.err.println("队列已满，将丢弃消息");
        }
    }

    class Dispatch extends Thread {

        @Override
        public void run() {
//            System.out.println("async statistics start ");
            while (true) {
                calculate(getNextGroupMessages());
            }
        }

        private Map<String, List<StopWatchMessageEvent>> getNextGroupMessages() { // 获取一组消息
            long start = System.currentTimeMillis();
            while (true) {
                messageQueue.drainTo(drainMessageQueue, 64);
                if (drainMessageQueue.isEmpty()) {
                    try {
                        String message = messageQueue.poll(timeSplice, TimeUnit.SECONDS);
                        if (message == null) {
                            break;
                        }
                        drainMessageQueue.add(message);
                        if (System.currentTimeMillis() - start > TimeUnit.SECONDS.toMillis(timeSplice)) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (System.currentTimeMillis() - start > TimeUnit.SECONDS.toMillis(timeSplice)) {
                    break;
                }
            }

            Map<String, List<StopWatchMessageEvent>> data = Maps.newHashMap();
            for (String message : drainMessageQueue) {
                if (message == null) continue;
                StopWatchMessageEvent watchMessage = stopWatchParse.parseStopWatch(message);
                if (message == null) continue;
                if (data.containsKey(watchMessage.getTag())) {
                    data.get(watchMessage.getTag()).add(watchMessage);
                } else {
                    List<StopWatchMessageEvent> stopWatchMessageEventList = Lists.newLinkedList();
                    stopWatchMessageEventList.add(watchMessage);
                    data.put(watchMessage.getTag(), stopWatchMessageEventList);
                }
            }
            drainMessageQueue.clear();
            return data;
        }
    }

    // TODO 需要异步处理，防止消息拥堵事被丢弃
    public void calculate(Map<String, List<StopWatchMessageEvent>> data) {
        List<StopWatchMetric> metricList = new LinkedList<StopWatchMetric>();
        for (String tag : data.keySet()) {
            List<StopWatchMessageEvent> stopWatchMessages = data.get(tag);
            StopWatchMetric message = new StopWatchMetric();
            message.setCount(stopWatchMessages.size());
            message.setTag(tag);
            if (message.getCount() == 0) continue;
            message.setTime(stopWatchMessages.get(0).getStart());
            long totalTime = 0;
            for (StopWatchMessageEvent watch : stopWatchMessages) {
                if (watch == null) continue;
                totalTime += watch.getTime() / NANOS_IN_A_MILLI;
                MetricsComponent.registry.histogram(watch.getTag()).update(watch.getTime() / NANOS_IN_A_MILLI);// 计算响应时间的Histograms
            }
            message.setMean(totalTime / message.getCount());
            // todo
            Histogram histogram = MetricsComponent.registry.getHistograms().get("aaa");
            if (histogram != null) {
                Snapshot snapshot = histogram.getSnapshot();
                message.setMin(snapshot.getMin());
                message.setT50(snapshot.getMedian());
                message.setT75(snapshot.get75thPercentile());
                message.setT95(snapshot.get95thPercentile());
                message.setT99(snapshot.get99thPercentile());
                message.setT999(snapshot.get999thPercentile());
                message.setMax(snapshot.getMax());
            }
            messageLoopOnChildAppender(message); // 将消息下发给下一个appender处理，这里需要一个异步处理的Appender，不可以阻塞
            metricList.add(message);
        }
    }
}



