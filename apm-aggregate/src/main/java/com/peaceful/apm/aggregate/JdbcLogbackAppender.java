package com.peaceful.apm.aggregate;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.AppenderBase;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.ibatis.common.jdbc.ScriptRunner;
import com.ibatis.common.resources.Resources;
import com.peaceful.boot.common.helper.DateHelper;
import com.peaceful.boot.common.helper.NetHelper;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * perf4jAppender for jdbc
 * 已验证支持Mysql、SQLite
 * <p>
 * 必须作为 {@link org.perf4j.log4j.AsyncCoalescingStatisticsAppender}的 subAppender
 *
 * @author WangJun
 * @version 1.0 16/6/24
 */
public class JdbcLogbackAppender extends AppenderBase<ch.qos.logback.classic.spi.LoggingEvent> {

    private String url;
    private String user;
    private String password;
    private String driver = "org.sqlite.JDBC";

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    private int state; // appender state
    private final static int NOT_INIT = 0;
    private final static int OK = 1;
    private final static int FAIL = 2;
    private String fail_message = null;
    private static JdbcLogbackAppender instance = null;
    private DB_TYPE_ENUM db_type_enum;
    public static final String TABLE = "APM_TIMING_STATISTICS";
    private final static String INSERT_SQL = "insert into " + TABLE + " (`tag`,`count`,`mean`,`min`,`max`,`std`,`interval`,`hostname`,`create_time`) values (?,?,?,?,?,?,?,?,?)";

    public void activateOptions() {
        if (StringUtils.isBlank(driver)) {
            state = FAIL;
            fail_message = "driver must be set";
            return;

        }
        if (driver.equals("org.sqlite.JDBC")) {
            db_type_enum = DB_TYPE_ENUM.SQLite;
        } else if (driver.equals("com.mysql.jdbc.Driver")) {
            db_type_enum = DB_TYPE_ENUM.MYSQL;
        } else {
            db_type_enum = DB_TYPE_ENUM.OTHER;
        }
        if (StringUtils.isBlank(url)) {
            state = FAIL;
            fail_message = "url must be set";
            return;
        }
        init();
    }


    private enum DB_TYPE_ENUM {
        MYSQL, SQLite, OTHER
    }

    public void save(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        if (timingStatisticsMap == null || timingStatisticsMap.isEmpty()) {
            return;
        }
        if (state == FAIL) {
            System.err.println("SQLite Gather has fatal error->" + fail_message);
        } else if (state == NOT_INIT) {
            activateOptions();
        }
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
            System.err.println("batch result is " + rows.length);
        } catch (Exception e) {
            Throwables.propagate(e);
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Throwables.propagate(e);
            }

        }
    }


    private void init() {
        state = FAIL;
        try {
            Class.forName(driver);
            System.err.println("driver[" + driver + "] load suc");
        } catch (ClassNotFoundException e) {
            fail_message = e.getMessage();
            Throwables.propagate(e);
        }
        Connection connection = null;
        try {
            connection = getConnection();
            if (db_type_enum == DB_TYPE_ENUM.SQLite) {
                String path = null;
                try {
                    path = url.split(":")[2];
                    Preconditions.checkArgument(StringUtils.isNoneBlank(path), "%s is a illegal url", url);
                    System.err.println("connection url is " + url);
                } catch (Exception e) {
                    fail_message = "sqlite url format must be: jdbc:sqlite:path  path is a file path";
                }
                try {
                    File file = new File(path);
                    File parent = file.getParentFile();
                    if (parent != null && !parent.exists()) {
                        if (!parent.mkdir()) {
                            fail_message = "can't create dir:" + parent.getAbsolutePath();
                            return;
                        }
                    }
                    System.err.println("sqlite file location is " + file.getAbsolutePath());
                } catch (Exception e) {
                    fail_message = "can't create file:" + path;
                    Throwables.propagate(e);
                }
                ScriptRunner runner = new ScriptRunner(connection, false, true);
                runner.setErrorLogWriter(new PrintWriter(System.err));
                runner.setLogWriter(null);
                runner.runScript(Resources.getResourceAsReader("sql/apm.sqlite.sql"));
            } else {
                ScriptRunner runner = new ScriptRunner(connection, false, false);
                runner.setErrorLogWriter(new PrintWriter(System.err));
                runner.setLogWriter(null);
                runner.runScript(Resources.getResourceAsReader("sql/apm.mysql.sql"));
            }
        } catch (Exception e) {
            fail_message = "can't create table " + TABLE;
            Throwables.propagate(e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    Throwables.propagate(e);
                }
            }
        }
        instance = this;
        System.err.println("apm aggregate[" + db_type_enum + "] init suc!");
        ApmTimingGatherExecutor.loadToJvm();
        state = OK;
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            if (db_type_enum == DB_TYPE_ENUM.SQLite) {
                connection = DriverManager.getConnection(url);
            } else {
                connection = DriverManager.getConnection(url, user, password);
            }
        } catch (SQLException e) {
            Throwables.propagate(e);
        }
        return connection;
    }

    @Override
    protected void append(LoggingEvent loggingEvent) {
        if ((loggingEvent.getArgumentArray() != null)
                && (loggingEvent.getArgumentArray().length > 0)) {
            Object logMessage = loggingEvent.getArgumentArray()[0];
            if (logMessage instanceof GroupedTimingStatistics) {
                GroupedTimingStatistics statistics = (GroupedTimingStatistics) logMessage;
                try {
                    save(statistics.getStatisticsByTag(), statistics.getStopTime() - statistics.getStartTime(), TimeUnit.MILLISECONDS);
                } catch (Exception e) {
                    System.err.println(Throwables.getStackTraceAsString(e));
                }
            }
        }
    }


    public static JdbcLogbackAppender getInstance() {
        return instance;
    }

    public MetricsSet select(String sql, String tag, long from, long to) {
        if (from == 0) from = System.currentTimeMillis() - 1000 * 60 * 60 * 1;
        if (to == 0) to = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (new Date(to).compareTo(DateHelper.getAddDayDate(new Date(from), 1)) > 0) {
            dateFormat = new SimpleDateFormat("MM/dd HH:mm");
        }
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(sql);
            statement.setString(1, NetHelper.getHostname());
            statement.setString(2, tag);
            statement.setLong(3, from);
            statement.setLong(4, to);
            resultSet = statement.executeQuery();
            MetricsSet metricsSet = null;
            while (resultSet.next()) {
                if (metricsSet == null) {
                    metricsSet = new MetricsSet(tag, resultSet.getString("interval"), resultSet.getString("hostname"));
                }
                metricsSet.counts.add(resultSet.getLong("count"));
                metricsSet.means.add(resultSet.getDouble("mean"));
                metricsSet.mins.add(resultSet.getLong("min"));
                metricsSet.maxs.add(resultSet.getLong("max"));
                metricsSet.stds.add(resultSet.getDouble("std"));
                metricsSet.series.add(dateFormat.format(new Date(resultSet.getLong("create_time"))));

            }
            return metricsSet;
        } catch (SQLException e) {
            Throwables.propagate(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Throwables.propagate(e);
            }

        }
        return null;
    }

    public List<String> selectTags() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement("select DISTINCT (`tag`) from " + TABLE + " where hostname = ?");
            statement.setString(1, NetHelper.getHostname());
            resultSet = statement.executeQuery();
            List<String> tags = new LinkedList<>();
            while (resultSet.next()) {
                tags.add(resultSet.getString(1));
            }
            return tags;
        } catch (SQLException e) {
            Throwables.propagate(e);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                Throwables.propagate(e);
            }

        }
        return null;
    }


}
