package com.peaceful.apm.test.appender;

import com.google.common.base.Throwables;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by wangjun38 on 2018/1/28.
 */
public abstract class MySqlMessageAppender implements MessageAppender {

    @Setter
    private String ip;
    @Setter
    private int port;
    @Setter
    private String db;
    @Setter
    private String user;
    @Setter
    private String password;

    @Getter
    private DataSource dataSource;

    public MySqlMessageAppender(String ip, int port, String db, String user, String password) {
        this.ip = ip;
        this.port = port;
        this.db = db;
        this.user = user;
        this.password = password;
        MysqlDataSource dataSource = new MysqlDataSource();

        try {
            dataSource.setLoginTimeout(3);// 3s超时
            dataSource.setServerName(ip);
            dataSource.setPort(port);
            dataSource.setDatabaseName(db);
            dataSource.setUser(user);
            dataSource.setPassword(password);
            this.dataSource = dataSource;
        } catch (Exception e) {
            System.err.println("mysql connection timeout:" + Throwables.getStackTraceAsString(e));
        }
    }

}
