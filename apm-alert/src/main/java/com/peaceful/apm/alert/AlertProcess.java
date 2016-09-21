package com.peaceful.apm.alert;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * alert 运行状态机
 * Created by wangjun on 16/9/14.
 */
public class AlertProcess {


    public State state; // 当前状态
    public long nextProcessTime; // 下次调度时间

    protected static ConcurrentHashMap<String, AlertProcess> processState = new ConcurrentHashMap<String, AlertProcess>();

    public static synchronized State getState(String tag) {
        AlertProcess process = processState.get(tag);
        if (process == null) {
            return State.TERMINATED;
        } else {
            return process.state;
        }
    }

    /**
     * 设置alert 定时任务实例终止，这并不会立即终止任务，它会等到下次调度时才可以终止任务的执行，比如之前设置1天执行一次，现在设置1分钟执行
     * 一次，设置生效的时间要到下次运行时才可以生效，这个等待时间有可能是一天的时间
     *
     * @param tag
     */
    public static synchronized void setTerminated(String tag) {
        AlertProcess process = processState.get(tag);
        if (process == null) {

        } else {
            process.state = State.WAIT_TERMINATED;
        }
    }

    public enum State {
        NEW, // alert 刚创建，等待执行
        START, // alert 已经被真正的执行过
        WAIT_TERMINATED,// 设置alert终止
        TERMINATED // alert已经终止
    }


}
