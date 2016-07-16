package com.peaceful.perf4j.dashboard.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * @author WangJun
 * @version 1.0 16/4/25
 */
public class SQLiteHelper {

    private static final String userDir = System.getProperty("user.dir");
    private static final String driver = "org.sqlite.JDBC";
    private static final String url = "jdbc:sqlite:"+userDir+"/apm-dashboard.db";
    private static Connection connection = null;
    private static final Object CONN_MONITOR = new Object();
    private static final Logger LOGGER = LoggerFactory.getLogger(SQLiteHelper.class);

    public static Connection getConn() {

        if (connection != null) {
            return connection;
        } else {
            synchronized (CONN_MONITOR) {
                if (connection != null) {
                    return connection;
                }
                try {
                    Class.forName(driver);
                    // 默认在当前目录下创建
                    connection = DriverManager.getConnection(url);
                    return connection;
                } catch (Exception e) {
                    System.err.println(e.getClass().getName() + ": " + e.getMessage());
                    LOGGER.error(ExceptionUtils.getRootCauseMessage(e));
                    return null;
                }
            }
        }
    }

    public static int createTable(String sql, String... indexs) throws SQLException {
        String table = sql.split("\\s+")[2];
        if (StringUtils.isBlank(table)) {
            return -1;
        }
        String existSql = String.format("select count(*)  from sqlite_master where type='table' and name = '%s' ", table);
        PreparedStatement preparedStatement = getConn().prepareStatement(existSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.getInt(1) > 0) {
            resultSet.close();
            preparedStatement.close();
            return 0;
        } else {
            resultSet.close();
            preparedStatement.close();
            execute(sql);
            if (indexs != null && indexs.length > 0) {
                for (String index : indexs) {
                    sql = String.format("CREATE INDEX %s_%s_index ON %s (%s)", table, index, table, index);
                    execute(sql);
                }
            }
            return 1;
        }
    }

    /**
     * execute sql
     *
     * @param sql
     * @throws SQLException
     */
    public static void execute(String sql) throws SQLException {
        PreparedStatement preparedStatement = getConn().prepareStatement(sql);
        LOGGER.info("executor sql {} ,result is {} ", sql, preparedStatement.execute());
        preparedStatement.close();
    }

    public static int executeUpdate(String sql) throws SQLException {
        PreparedStatement preparedStatement = getConn().prepareStatement(sql);
        int rows = preparedStatement.executeUpdate();
        LOGGER.info("executor sql {} ,result is {} ", sql, rows);
        preparedStatement.close();
        return rows;
    }

}
