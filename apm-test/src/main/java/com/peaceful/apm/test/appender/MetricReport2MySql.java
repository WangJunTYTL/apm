package com.peaceful.apm.test.appender;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by wangjun38 on 2018/1/28.
 */
public class MetricReport2MySql extends MySqlMessageAppender {


    public MetricReport2MySql(String ip, int port, String db, String user, String password) {
        super(ip, port, db, user, password);
    }

    public void send(Object message) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = getDataSource().getConnection();
            statement = connection.prepareStatement("INSERT INTO user (`name`) VALUES (?)");
            statement.setString(1, "test");
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
