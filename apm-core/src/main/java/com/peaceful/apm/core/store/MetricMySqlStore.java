package com.peaceful.apm.core.store;

import com.peaceful.apm.core.Counter;
import com.peaceful.apm.core.MetricStore;
import com.peaceful.apm.core.TimingWatchHandler;
import com.peaceful.apm.core.helper.Log;
import com.peaceful.apm.core.helper.NetHelper;

import org.perf4j.TimingStatistics;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wangjun on 16/9/8.
 */
public class MetricMySqlStore implements MetricStore {

    private String url;
    private String user;
    private String password;
    private String service;
    private static boolean needStart = true; // 是否需要启动
    private static boolean isRun = false; //是否已启动
    private static int retryTime = 0; //是否已启动

    public MetricMySqlStore(String url, String user, String password, String driver, String service) {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            Log.warn(e);
            needStart = false;
        }
        this.url = url;
        this.user = user;
        this.password = password;
        this.service = service;
        Log.info("start ");
    }

    private synchronized boolean isStart() {
        if (isRun) {
            return true;
        }
        if (needStart) {
            try {
                registerService();
                initTable();
                saveWatchData();
                needStart = false;
                isRun = true;
            } catch (Exception e) {
                isRun = false;
                retryTime++;
                if (retryTime == 3) needStart = false;
            }
        } else {
            Log.info("MetricMySqlStore is running");
        }
        return isRun;
    }

    private void initTable() {
        int routeId = getTableRoute(service);
        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute(DAO.createApmServiceTable());
            statement.execute(DAO.createApmStatisticsTable(routeId));
            statement.execute(DAO.createApmHeartbeatTable(routeId));
        } catch (SQLException e) {
            Log.warn("init table:" + e);
        } finally {
            closeConnection(statement, connection);
        }
    }

    private int getTableRoute(String service) {
        String query_sql = "select id from apm_service_route where service = ? limit 1";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int tableId = -1;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(query_sql);
            statement.setString(1, service);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                tableId = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            Log.warn(e);
        } finally {
            closeResult(resultSet, null);
            closeConnection(statement, connection);
        }
        return tableId;
    }

    public void saveStatisticsData(Map<String, TimingStatistics> timingStatisticsMap, long interval, TimeUnit timeUnit) {
        if (!isStart()) return;
        /*String statisticsTable = "apm_statistics_" + getTableRoute(service);
        String INSERT_SQL = "insert into " + statisticsTable + " (`tag`,`count`,`mean`,`min`,`max`,`std`,`interval`,`hostname`,`create_time`) values (?,?,?,?,?,?,?,?,?)";
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
                statement.setLong(9, System.currentTimeMillis() / 1000);
                statement.addBatch();
            }
            statement.executeBatch();*/
        String heartbeatTable = "apm_heartbeat_" + getTableRoute(service);
        String INSERT_SQL = "insert into " + heartbeatTable + " (`hostname`,`tag`,`metric`,`value`,`create_time`) values (?,?,?,?,?)";
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getConnection();
            statement = connection.prepareStatement(INSERT_SQL);
            Iterator<String> tags = timingStatisticsMap.keySet().iterator();
            String hostname = NetHelper.getHostname();
            long unixTime = System.currentTimeMillis() / 1000;
            while (tags.hasNext()) {
                String tag = tags.next();
                TimingStatistics timingStatistics = timingStatisticsMap.get(tag);

                statement.setString(1, hostname);
                statement.setString(2, tag);
                statement.setString(3, "TPS");
                BigDecimal bg = new BigDecimal((double) timingStatistics.getCount() / 60);
                double tps = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                statement.setDouble(4, tps);
                statement.setLong(5, unixTime);
                statement.addBatch();

                statement.setString(1, hostname);
                statement.setString(2, tag);
                statement.setString(3, "Mean");
                bg = new BigDecimal(timingStatistics.getMean());
                double mean = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                statement.setDouble(4, mean);
                statement.setLong(5, unixTime);
                statement.addBatch();

                statement.setString(1, hostname);
                statement.setString(2, tag);
                statement.setString(3, "Max");
                statement.setDouble(4, timingStatistics.getMax());
                statement.setLong(5, unixTime);
                statement.addBatch();

                statement.setString(1, hostname);
                statement.setString(2, tag);
                statement.setString(3, "Min");
                statement.setDouble(4, timingStatistics.getMin());
                statement.setLong(5, unixTime);
                statement.addBatch();

                statement.setString(1, hostname);
                statement.setString(2, tag);
                statement.setString(3, "Std");
                bg = new BigDecimal(timingStatistics.getStandardDeviation());
                double std = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                statement.setDouble(4, std);
                statement.setLong(5, unixTime);
                statement.addBatch();
                statement.executeBatch();
            }
        } catch (Exception e) {
            Log.debug(e);
        } finally {
            closeConnection(statement, connection);
        }
    }

    public void saveWatchData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    TimingWatchHandler watchHandler = new TimingWatchHandler();
                    if (watchHandler.isStart()) {
                        Log.debug("start watch handler service");
                    }
                    while (watchHandler.isStart()) {
                        List<Counter> heartBeatDataList = watchHandler.getNext();
                        String heartbeatTable = "apm_heartbeat_" + getTableRoute(service);
                        String INSERT_SQL = "insert into " + heartbeatTable + " (`hostname`,`tag`,`metric`,`value`,`create_time`) values (?,?,?,?,?)";
                        Connection connection = null;
                        PreparedStatement statement = null;
                        try {
                            connection = getConnection();
                            statement = connection.prepareStatement(INSERT_SQL);
                            for (Counter data : heartBeatDataList) {
                                statement.setString(1, NetHelper.getHostname());
                                statement.setString(2, data.getTag());
                                statement.setString(3, data.getMetric());
                                BigDecimal bg = new BigDecimal(data.getValue());
                                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                                statement.setDouble(4, value);
                                statement.setLong(5, data.getCreateTime());
                                statement.addBatch();
                            }
                            statement.executeBatch();
                        } catch (Exception e) {
                            Log.debug(e);
                        } finally {
                            closeConnection(statement, connection);
                        }
                    }
                } catch (Exception e) {
                    Log.warn(e);
                }
            }
        }.start();
    }

    private synchronized void registerService() {
        String apm_service_route = "CREATE TABLE IF NOT EXISTS apm_service_route (" +
                "  `id`  int NOT NULL PRIMARY KEY  AUTO_INCREMENT," +
                "  `service`  VARCHAR(50) NOT NULL ," +
                "  UNIQUE KEY (service)" +
                ")ENGINE=MyISAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";

        Connection connection = null;
        Statement statement = null;
        try {
            connection = getConnection();
            statement = connection.createStatement();
            statement.execute(apm_service_route);
        } catch (Exception e) {
            // ignore
            Log.warn(e);
        } finally {
            closeConnection(statement, connection);
        }
        PreparedStatement insertServiceStatement = null;
        PreparedStatement insertRouteStatement = null;
        try {
            connection = getConnection();
            insertServiceStatement = connection.prepareStatement("insert INTO apm_service (`service`,`hostname`) VALUES (?,?)");
            insertServiceStatement.setString(1, service);
            insertServiceStatement.setString(2, NetHelper.getHostname());
            insertServiceStatement.execute();
        } catch (SQLException e) {
            Log.debug(e);
        } finally {
            closeConnection(insertServiceStatement, null);
        }
        try {
            insertRouteStatement = connection.prepareStatement("insert INTO apm_service_route (`service`) VALUES (?)");
            insertRouteStatement.setString(1, service);
            insertRouteStatement.execute();
        } catch (SQLException e) {
            // .ignore
            Log.debug(e);
        } finally {
            closeConnection(insertRouteStatement, connection);
        }

    }

    private Connection getConnection() throws SQLException {
        Connection connection;
        if (user == null || "".equals(user)) {
            connection = DriverManager.getConnection(url);
        } else {
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }

    private void closeConnection(Statement statement, Connection connection) {
        try {
            if (statement != null && !statement.isClosed()) statement.close();
            if (connection != null && !connection.isClosed()) connection.close();
        } catch (SQLException e) {
            Log.debug(e);
        }
    }

    private void closeResult(ResultSet resultSet, Statement statement) {
        try {
            if (resultSet != null && !resultSet.isClosed()) resultSet.close();
            if (statement != null && !statement.isClosed()) statement.close();
        } catch (SQLException e) {
            // ignore
            Log.debug(e);
        }
    }
}
