package com.peaceful.apm.dashboard.conf;

import com.ibatis.common.jdbc.ScriptRunner;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.spi.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by wangjun on 16/9/6.
 */
@Configuration
public class SqlSessionManage {


    private Logger logger = org.slf4j.LoggerFactory.getLogger(getClass());

    @Bean(name = "datasourceSqlSessionFactory")
    public SqlSessionFactory getDataSourceSqlSessionFactory() throws IOException, SQLException {
        // init databases
        String resource = "apm/mybatis-datasource-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        // init create table if not exist
        SqlSession session = sqlSessionFactory.openSession(true);
        Connection connection = session.getConnection();
        ScriptRunner runner = new ScriptRunner(connection, false, true);
        runner.setErrorLogWriter(new PrintWriter(System.err));
        runner.setLogWriter(null);
        runner.runScript(Resources.getResourceAsReader("apm/mysql.dashboard.config.sql"));
        session.close();
        return sqlSessionFactory;
    }

}
