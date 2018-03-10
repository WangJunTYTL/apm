package com.peaceful.apm.test.appender;

import java.sql.SQLException;

/**
 * Created by wangjun38 on 2018/1/28.
 */
public class MetricReport2MySqlTest {

    @org.junit.Test
    public void send() throws SQLException {
        MetricReport2MySql metricReport2MySql = new MetricReport2MySql("localhost",3306,"test","root","110");
        metricReport2MySql.send("test");
    }
}