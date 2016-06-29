package com.peaceful.perf4j.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.peaceful.common.util.ExceptionUtils;
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
import java.util.ArrayList;
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
    private static final String INSERT_LINK_SQL = "INSERT INTO LINKS (`name`,`url`,`location`) VALUES ('%s', '%s', %d);";
    private static final String DELETE_NODE_SQL = "DELETE FROM CLUSTER_NODES WHERE cluster_name = '%s' and node_name = '%s';";
    private static final String DELETE_LINK_SQL = "DELETE FROM LINKS WHERE name = '%s';";
    private static final String SELECT_NODE_SQL = "SELECT * FROM CLUSTER_NODES ORDER BY cluster_name;";
    private static final String SELECT_LINK_SQL = "SELECT * FROM LINKS;";
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfController.class);

    @RequestMapping("/cluster")
    @Description("获取集群列表")
    public void getClusterConf() throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLiteHelper.getConn().prepareStatement(SELECT_NODE_SQL);
            resultSet = statement.executeQuery();
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
            Http.responseJSON(0, JSON.toJSONString(nodes));
        } finally {
            resultSet.close();
            statement.close();
        }
    }

    @RequestMapping("/cluster/add")
    @Description("加入新的机器")
    public void addNodes(String clusterName, String nodeName, String url) {
        try {
            SQLiteHelper.executeUpdate(String.format(INSERT_NODE_SQL, clusterName, nodeName, url, System.currentTimeMillis()));
            Conf.NODES.put(nodeName, url);
            Http.responseJSON(0, "OK");
        } catch (SQLException e) {
            LOGGER.error("add node error: {}", e);
            Http.responseJSON(1, "FAIL");
        }
    }

    @RequestMapping("/delete")
    @Description("删除机器或集群或节点")
    public void deleteNodes(String clusterName, String nodeName, String linkName, int type) {
        try {
            if (type == 0) {
                int rows = SQLiteHelper.executeUpdate(String.format(DELETE_NODE_SQL, clusterName, nodeName));
                Conf.NODES.remove(nodeName);
                Http.responseJSON(0, rows);
            } else {
                int rows = SQLiteHelper.executeUpdate(String.format(DELETE_LINK_SQL, linkName));
                Http.responseJSON(0, rows);
            }
        } catch (SQLException e) {
            LOGGER.error("delete node error: {}", e);
            Http.responseJSON(1, "FAIL");
        }
    }


    @RequestMapping("/links/add")
    @Description("加入快捷链接")
    public void addLinks(String name, String url, int location) {
        try {
            SQLiteHelper.executeUpdate(String.format(INSERT_LINK_SQL, name, url, 0));
            Http.responseJSON(0, "OK");
        } catch (SQLException e) {
            LOGGER.error("add link error: {}", ExceptionUtils.getStackTrace(e));
            Http.responseJSON(1, "FAIL");
        }
    }

    @RequestMapping("/links/get")
    @Description("获取快捷链接")
    public void getLinks() throws SQLException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = SQLiteHelper.getConn().prepareStatement(SELECT_LINK_SQL);
            resultSet = statement.executeQuery();
            List<JSONObject> nodes = new ArrayList<JSONObject>();
            while (resultSet.next()) {
                String name = resultSet.getString(1);
                JSONObject object = new JSONObject();
                object.put("name", name);
                object.put("url", resultSet.getString(2));
                nodes.add(object);
            }
            Http.responseJSON(0, nodes);
        } finally {
            resultSet.close();
            statement.close();
        }
    }

}
