package com.peaceful.apm.core;

import java.util.List;

/**
 * Created by wangjun38 on 2017/9/25.
 */
public abstract class TimingWatch {

    public int interval() {
        return 1; // 定时获取数据间隔
    }

    public abstract List<Counter> lap();

}
