package com.peaceful.apm.core;


import com.peaceful.apm.core.helper.Log;

/**
 * 实现该接口，可以定时把数据写入到你当前配置的appender中
 *
 * @author WangJun
 * @version 1.0 16/6/26
 */
public abstract class HeartbeatTask implements Runnable {


    /**
     * 上报频率，单位分钟
     */
    public abstract int rate();

    public abstract void upload();

    @Override
    public void run() {
        try {
            upload();
        } catch (Exception e) {
            Log.debug(e);
        }
    }
}
