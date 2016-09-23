package com.peaceful.apm.aggregate;

import com.google.common.base.Throwables;
import com.peaceful.boot.common.helper.NetHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.helpers.LogLog;
import org.perf4j.TimingStatistics;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun on 16/9/8.
 */
public class DBService implements AppenderService {

    private String url;
    private String user;
    private String password;
    private String service;

    public DBService(String url, String user, String password, String driver, String service) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            Throwables.propagate(e);
        }
        this.url = url;
        this.user = user;
        this.password = password;
        this.service = service;
        initTable();
        initService();
    }

    private void initTable() {
        String apm_statistics = "CREATE TABLE IF NOT EXISTS APM_STATISTICS_" + service + "(" +
                "  `tag`         VARCHAR(255) NOT NULL," +
                "  `count`       INT         DEFAULT 0," +
                "  `mean`        DOUBLE      DEFAULT 0," +
                "  `min`         BIGINT      DEFAULT 0," +
                "  `max`         BIGINT      DEFAULT 0," +
                "  `std`         DOUBLE      DEFAULT 0," +
                "  `interval`    INT         DEFAULT 0," +
                "  `hostname`    VARCHAR(100) DEFAULT 'Unknown'," +
                "  `create_time` BIGINT       NOT NULL," +
                "  INDEX (tag)," +
                "  INDEX (hostname)," +
                "  INDEX (create_time)" +
                "  )ENGINE=MyISAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";

        String apm_service = "CREATE TABLE IF NOT EXISTS APM_SERVICE (" +
                "  `service`  VARCHAR(50) NOT NULL PRIMARY KEY ," +
                "  `hostname` VARCHAR(100)" +
                ")ENGINE=MyISAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";
        Connection connection = getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            statement.execute(apm_statistics);
            statement.execute(apm_service);
        } catch (SQLException e) {
            Throwables.propagate(e);
        } finally {
            try {
                statement.close();
                connection.close();
            } catch (SQLException e) {
                Throwables.propagate(e);
            }
        }
    }

    public void saveStatisticsData(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        String INSERT_SQL = "insert into APM_STATISTICS_" + service + " (`tag`,`count`,`mean`,`min`,`max`,`std`,`interval`,`hostname`,`create_time`) values (?,?,?,?,?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(INSERT_SQL);
            Iterator<String> tags = timingStatisticsMap.keySet().iterator();
            while (tags.hasNext()) {
                String tag = tags.next();
                TimingStatistics timingStatistics = timingStatisticsMap.get(tag);
                statement.setString(1, tag);
                statement.setLong(2, timingStatistics.getCount());
                BigDecimal bg = new BigDecimal(timingStatistics.getMean());
                double mean = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                statement.setDouble(3, mean);
                statement.setLong(4, timingStatistics.getMin());
                statement.setLong(5, timingStatistics.getMax());
                bg = new BigDecimal(timingStatistics.getStandardDeviation());
                double std = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                statement.setDouble(6, std);
                statement.setDouble(7, TimeUnit.SECONDS.convert(interval, timeUnit));
                statement.setString(8, NetHelper.getHostname());
                statement.setLong(9, System.currentTimeMillis());
                statement.addBatch();
            }
            int[] rows = statement.executeBatch();
            LogLog.debug("batch result is " + rows.length);
        } catch (Exception e) {
            System.err.print(Throwables.getStackTraceAsString(e));
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.print(Throwables.getStackTraceAsString(e));
            }
        }
    }

    private void initService() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement("insert INTO APM_SERVICE (`service`,`hostname`) VALUES (?,?)");
            preparedStatement.setString(1, service);
            preparedStatement.setString(2, NetHelper.getHostname());
            preparedStatement.execute();
        } catch (SQLException e) {
            // .ignore
        } finally {
            try {
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Throwables.propagate(e);
            }
        }
    }

    private Connection getConnection() {
        Connection connection = null;
        try {
            if (StringUtils.isNotBlank(user)) {
                connection = DriverManager.getConnection(url, user, password);
            } else {
                connection = DriverManager.getConnection(url);
            }
        } catch (SQLException e) {
            Throwables.propagate(e);
        }
        return connection;
    }
}
