package com.peaceful.apm.aggregate.jdbc;

import com.peaceful.apm.aggregate.ElasticLog4jAppender;
import org.junit.Test;

/**
 * @author WangJun
 * @version 1.0 16/6/25
 */
public class Perf4jElasticSeachLog4jAppenderTest {

    static ElasticLog4jAppender perf4jElasticSeachLog4jAppender = new ElasticLog4jAppender();

    static {
        perf4jElasticSeachLog4jAppender.setUrl("127.0.0.1");
        perf4jElasticSeachLog4jAppender.setPort(9300);
        perf4jElasticSeachLog4jAppender.setClusterName("elasticsearch");
    }

    @Test
    public void activateOptions() throws Exception {
        perf4jElasticSeachLog4jAppender.activateOptions();
    }

}