package com.peaceful.apm.aggregate.spi;

import org.perf4j.TimingStatistics;

import java.util.Map;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public interface TimingAggregate {

    Map<String, TimingStatistics> get();

}
