package com.peaceful.apm.aggregate.plugin.spi;

import org.perf4j.TimingStatistics;

import java.util.Map;

/**
 * 实现该接口，可以定时把数据写入到你当前配置的appender中
 *
 * @author WangJun
 * @version 1.0 16/6/26
 */
public interface TimingAggregate {

    Map<String, TimingStatistics> get();

}
