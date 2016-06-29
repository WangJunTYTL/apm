package com.peaceful.apm.aggregate;

import com.peaceful.boot.common.helper.Console;
import org.junit.Test;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class BaiduGraphFormatTest {


    private static JdbcLog4jAppender jdbcLog4jAppender = new JdbcLog4jAppender();

    static {
        jdbcLog4jAppender.setUrl("jdbc:mysql://127.0.0.1:3306/test3?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false");
        jdbcLog4jAppender.setUser("root");
        jdbcLog4jAppender.setDriver("com.mysql.jdbc.Driver");
        jdbcLog4jAppender.activateOptions();
    }


    @Test
    public void convert() throws Exception {
        MetricsSet metricsSet = MetricsFromJdbc.selectMetrics("http.index",1466904572520L,System.currentTimeMillis());
        Console.log(BaiduGraphFormat.convert(metricsSet));
    }


    @Test
    public void selectMetricsTag() throws Exception {
        Console.log(MetricsFromJdbc.selectMetricsTag());
    }

}