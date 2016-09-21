package com.peaceful.apm.alert;

import java.util.Map;

/**
 * 监控数据源,对于采集的的监控数据以Map kv的方式提供给Alert模块,Alert模块可以方便的以简单表达式的方式处理kv的数据格式
 *
 * @author WangJun
 * @version 1.0 16/6/21
 */
public interface MetricCallback {

    /**
     * 数据源以kv的方式返回
     *
     * @return
     */
    Map<String, Object> get();
}
