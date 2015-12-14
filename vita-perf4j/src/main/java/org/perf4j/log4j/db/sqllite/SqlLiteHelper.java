package org.perf4j.log4j.db.sqllite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 导入性能数据到sqllite中
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 15/12/10
 */
public class SqlLiteHelper {


    private static final int INIT_SUC = 1;
    private static final int NOT_INIT = 0;
    private static final int INIT_FAIL = -1;


    private static String path = "";

    private static Connection connection = null;

    private static int state = NOT_INIT;


    public void setPath(String path) {
        SqlLiteHelper.path = path;
    }

    private SqlLiteHelper() {

    }


    public static SqlLiteHelper getInstance(String path) throws Exception {
        if (state == INIT_SUC) {
            return Single.sqlLiteHelper;
        } else if (state == INIT_FAIL) {
            throw new Exception("can't init sqllite !");
        } else {
            try {
                SqlLiteHelper sqlLiteHelper = Single.sqlLiteHelper;
                sqlLiteHelper.initConn(path);
                sqlLiteHelper.initTable();
                System.out.print("init sqllite suc ,path is " + path);
                state = INIT_SUC;
                return sqlLiteHelper;
            } catch (Exception e) {
                state = INIT_FAIL;
                throw e;
            }
        }
    }

    public static SqlLiteHelper getInstance() {
        if (state == INIT_SUC) {
            return Single.sqlLiteHelper;
        }
        return null;
    }

    private static class Single {
        public static SqlLiteHelper sqlLiteHelper = new SqlLiteHelper();
    }

    private synchronized void initConn(String path) throws Exception {
        if (connection == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                // 默认在当前目录下创建
                connection = DriverManager.getConnection("jdbc:sqlite:".concat(path));
            } catch (Exception e) {
                throw e;
            }
            System.out.println("Opened database successfully");
        }
    }

    private synchronized void initTable() throws SQLException {

        String createSql = "CREATE TABLE perf4j " +
                "(tag            VARCHAR(255)     NOT NULL," +
                " count          INT    NOT NULL, " +
                " mean           VARCHAR(255)     NOT NULL, " +
                " create_time        DATETIME)";
        String existSql = "select count(*)  from sqlite_master where type='table' and name = 'perf4j'";

        String tagIndex = "CREATE INDEX tag_index ON perf4j (tag)";
        String timeIndex = "CREATE INDEX time_index ON perf4j (create_time)";
        boolean isExist = false;
        PreparedStatement preparedStatement = connection.prepareStatement(existSql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (resultSet.getInt(1) > 0)
                isExist = true;
            resultSet.close();
            preparedStatement.close();
        }
        if (isExist) {
            System.out.println("table perf4j is exist");
        } else {
            preparedStatement = connection.prepareStatement(createSql);
            System.out.println("create table perf4j result is " + preparedStatement.execute());
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(tagIndex);
            System.out.println("create index tag result is " + preparedStatement.execute());
            preparedStatement.close();
            preparedStatement = connection.prepareStatement(timeIndex);
            System.out.println("create index time result is " + preparedStatement.execute());
            preparedStatement.close();
        }
    }

    public void save(GroupedTimingStatistics groupedTimingStatistics) throws SQLException {
        String sql = "INSERT INTO perf4j (tag,`count`,mean,create_time) " +
                "VALUES (?, ?, ?, ?);";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        for (Map.Entry<String, TimingStatistics> tagWithTimingStatistics : groupedTimingStatistics.getStatisticsByTag().entrySet()) {
            String tag = tagWithTimingStatistics.getKey();
            if (StringUtils.isEmpty(tag)) continue;
            TimingStatistics timingStatistics = tagWithTimingStatistics.getValue();
            preparedStatement.setString(1, tag);
            preparedStatement.setInt(2, timingStatistics.getCount());
            preparedStatement.setString(3, String.format("%.2f",timingStatistics.getMean()));
            preparedStatement.setDate(4, new Date(groupedTimingStatistics.getStopTime()));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    public String getData(Date from, Date to, String tag) throws SQLException {
        if (from == null) from = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 6);
        if (to == null) to = new Date(System.currentTimeMillis());
        String sql = "SELECT * from perf4j WHERE `tag` = ? AND `create_time` BETWEEN ? AND ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, tag);
        preparedStatement.setDate(2, from);
        preparedStatement.setDate(3, to);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> labels = new ArrayList<String>();
        List<Integer> countDatas = new ArrayList<Integer>();
        List<String> meanDatas = new ArrayList<String>();
        List<String> tags = new ArrayList<String>();


        while (resultSet.next()) {
            Long label = resultSet.getLong(4);
            labels.add(DateFormatUtils.format(new java.util.Date(label), "HH:mm:ss"));
            countDatas.add(resultSet.getInt(2));
            meanDatas.add(resultSet.getString(3));
        }

        resultSet.close();
        preparedStatement.close();

        tags.add(tag);

        JSONArray array = new JSONArray();
        JSONObject countData = new JSONObject();
        JSONObject meanData = new JSONObject();


        countData.put("graphType", "Count");
        countData.put("tags", tags);
        countData.put("labels", labels);
        countData.put("tagsToYData", countDatas);

        meanData.put("graphType", "Mean");
        meanData.put("tags", tags);
        meanData.put("labels", labels);
        meanData.put("tagsToYData", meanDatas);

        array.add(countData);
        array.add(meanData);

        JSONObject totalData = new JSONObject();
        totalData.put("graph",array);
        totalData.put("tags",getAllTags());

        return JSON.toJSONString(totalData);

    }

    public List<String> getAllTags() throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT (tag) from perf4j");
        List<String> tags = new ArrayList<String>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()){
            tags.add(resultSet.getString(1));
        }
        resultSet.close();
        preparedStatement.close();
        return tags;
    }




}
