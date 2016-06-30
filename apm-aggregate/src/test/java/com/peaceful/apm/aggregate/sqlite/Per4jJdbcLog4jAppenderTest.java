package com.peaceful.apm.aggregate.sqlite;

import com.peaceful.apm.aggregate.JdbcLog4jAppender;
import com.peaceful.apm.aggregate.MetricsForTag;
import com.peaceful.apm.aggregate.MetricsSet;
import com.peaceful.apm.aggregate.MetricsFromJdbc;
import com.peaceful.boot.common.helper.Console;
import org.junit.Test;
import org.perf4j.TimingStatistics;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/6/24
 */
public class Per4jJdbcLog4jAppenderTest {


    private static JdbcLog4jAppender sqliteAppender = new JdbcLog4jAppender();
    private static int db_mysql = 1;
    private static int db_sqlite = 0;
//    private static int db_type = db_sqlite;
    private static int db_type = db_mysql;

    static {
        if (db_mysql == db_type) {
            sqliteAppender.setUrl("jdbc:mysql://127.0.0.1:3306/test3?useUnicode=true&amp;characterEncoding=utf8&useSSL=false");
            sqliteAppender.setPassword(null);
            sqliteAppender.setUser("root");
            sqliteAppender.setDriver("com.mysql.jdbc.Driver");
        } else {
            sqliteAppender.setUrl("jdbc:sqlite:/data/logs/apm02.db");
        }
        sqliteAppender.activateOptions();
    }

    @org.junit.Test
    public void selectOneMetrics() throws Exception {
        Map<String, TimingStatistics> timingStatisticsMap = new HashMap<>();
        TimingStatistics timingStatistics = new TimingStatistics(1.2, 2.0, 3, 1, 6);
        timingStatisticsMap.put("test-tag", timingStatistics);
        MetricsFromJdbc.insertMetrics(timingStatisticsMap, 1, TimeUnit.MINUTES);
        MetricsForTag metrics = MetricsFromJdbc.selectOneMetrics("test-tag", 1465816843253L, System.currentTimeMillis());
        Console.log(metrics);
    }

    @Test
    public void selectMetrics() throws Exception {
        MetricsSet list = MetricsFromJdbc.selectMetrics("test-tag", 1465816843253L, System.currentTimeMillis());
        Console.log(list);
    }


}