package com.peaceful.perf4j.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.peaceful.common.util.Http;
import com.peaceful.perf4j.dashboard.common.Conf;
import com.peaceful.perf4j.dashboard.common.SQLiteHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author WangJun
 * @version 1.0 16/4/25
 */
@Controller
@ResponseBody
@RequestMapping("conf")
public class ConfController {

    private static final String INSERT_NODE_SQL = "INSERT INTO CLUSTER_NODES (cluster_name,`node_name`,`url`,create_time) VALUES ('%s', '%s', '%s', %s);";
    private static final String DELETE_NODE_SQL = "DELETE FROM CLUSTER_NODES WHERE cluster_name = '%s' and node_name = '%s';";
    private static final String SELECT_NODE_SQL = "SELECT * FROM CLUSTER_NODES ORDER BY cluster_name;";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfController.class);

    @RequestMapping("/cluster")
    @Description("获取集群列表")
    public void getClusterConf() throws SQLException {
        PreparedStatement statement = SQLiteHelper.getConn().prepareStatement(SELECT_NODE_SQL);
        ResultSet resultSet = statement.executeQuery();
        Map<String, List<JSONObject>> nodes = new TreeMap<String, List<JSONObject>>();
        while (resultSet.next()) {
            String clusterName = resultSet.getString(1);
            JSONObject object = new JSONObject();
            object.put("cluster_name", clusterName);
            object.put("node_name", resultSet.getString(2));
            if (nodes.containsKey(clusterName)) {
                nodes.get(clusterName).add(object);
            } else {
                nodes.put(clusterName, Lists.newArrayList(object));
            }
        }
        resultSet.close();
        statement.close();
        Http.responseJSON(0, JSON.toJSONString(nodes));
    }

    @RequestMapping("/cluster/add")
    @Description("加入新的机器")
    public void addNodes(String clusterName, String nodeName, String url) {
        try {
            SQLiteHelper.executeUpdate(String.format(INSERT_NODE_SQL, clusterName, nodeName, url, System.currentTimeMillis()));
            Conf.NODES.put(nodeName,url);
            Http.responseJSON(0, "OK");
        } catch (SQLException e) {
            LOGGER.error("add node error: {}", e);
            Http.responseJSON(1, "FAIL");
        }
    }

    @RequestMapping("/cluster/delete")
    @Description("删除新的机器")
    public void deleteNodes(String clusterName, String nodeName) {
        try {
            SQLiteHelper.executeUpdate(String.format(DELETE_NODE_SQL, clusterName, nodeName));
            Conf.NODES.remove(nodeName);
            Http.responseJSON(0, "OK");
        } catch (SQLException e) {
            LOGGER.error("delete node error: {}", e);
            Http.responseJSON(1, "FAIL");
        }
    }

}
