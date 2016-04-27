package com.peaceful.perf4j.dashboard.controller;

import com.peaceful.perf4j.dashboard.common.Conf;
import com.peaceful.perf4j.dashboard.common.SQLiteHelper;
import com.peaceful.perf4j.dashboard.common.TaskConsoleAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author WangJun
 * @version 1.0 16/4/25
 */
@Controller
@Description("Web程序启动时需要加载项")
public class OnloadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnloadController.class);
    private static final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
    private static final String CLUSTER_NODES_TABLE_SQL = "CREATE TABLE CLUSTER_NODES (" +
            " cluster_name            VARCHAR(255)     NOT NULL," +
            " node_name          INT    NOT NULL, " +
            " url           VARCHAR(255)     NOT NULL, " +
            " create_time        BIGINT)";
    private static final String SELECT_NODE_SQL = "SELECT * FROM CLUSTER_NODES ORDER BY cluster_name;";

    static {
        LOGGER.info("web onload ...");
        LOGGER.info("validate table cluster_nodes is exist...");
        try {
            SQLiteHelper.createTable(CLUSTER_NODES_TABLE_SQL, "cluster_name", "node_name");
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("WEB ONLOAD ERROR: CREATE TABLE CLUSTER_NODES FAIL -> {}", e);
        }
        executorService.scheduleAtFixedRate(new NodesRefreshInterval(), 0, 8, TimeUnit.SECONDS);
        executorService.scheduleAtFixedRate(new TagRefreshInterval(), 0, 2, TimeUnit.HOURS);


    }

    static class NodesRefreshInterval implements Runnable {

        @Override
        public void run() {
            try {
                PreparedStatement statement = null;
                statement = SQLiteHelper.getConn().prepareStatement(SELECT_NODE_SQL);
                ResultSet resultSet = statement.executeQuery();
                Conf.NODES.clear();
                while (resultSet.next()) {
                    Conf.NODES.put(resultSet.getString(2), resultSet.getString(3));
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                LOGGER.error("WEB ONLOAD ERROR: GET NODES ERROR ->{}", e);
            }
        }
    }

    static class TagRefreshInterval implements Runnable {

        @Override
        public void run() {
            try {
                for (String url : Conf.NODES.values()) {
                    TaskConsoleAPI.setUrl(url);
                    String runningInfo = TaskConsoleAPI.cat("history" + "&from=&to=&tag=");
                }
            } catch (Exception e) {
                LOGGER.error("WEB ONLOAD ERROR: GET TAG ERROR ->{}", e);
            }
        }
    }


}
