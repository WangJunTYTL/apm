package com.peaceful.apm.test.metric;

import com.codahale.metrics.MetricRegistry;

/**
 * 引入Metric组件：
 *
 * MetricsComponent is a Java library which gives you unparalleled insight into what your code does in production.
 * 更多信息：http://metrics.dropwizard.io/4.0.0/index.html
 *
 * Created by wangjun38 on 2018/1/28.
 */
public class MetricsComponent {

    final public static MetricRegistry registry = new MetricRegistry();
}
