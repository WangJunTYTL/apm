package com.peaceful.apm.alert;

import java.util.Map;

/**
 * 监控数据源
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
    Map<String, String> get();
}
