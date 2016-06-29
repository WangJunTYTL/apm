package org.perf4j.db.sqlite;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.common.util.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;
import org.perf4j.jvm.JVMGraphData;
import org.perf4j.jvm.JVMGraphData02;
import org.perf4j.jvm.JVMInfo;
import org.perf4j.servlet.RequestScopeManage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/1/21.
 */
public class GraphDataTables {

    public final static String perf4jTable = "perf4j";
    public final static String jvmTable = "jvm";

    private Connection connection;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public GraphDataTables(Connection connection) {
        this.connection = connection;
    }

    /**
     * load perf4j table
     *
     * @throws SQLException
     */
    public synchronized void loadPerf4j() throws SQLException {

        String createSql = "CREATE TABLE perf4j " +
                "(tag            VARCHAR(255)     NOT NULL," +
                " count          INT    NOT NULL, " +
                " mean           VARCHAR(255)     NOT NULL, " +
                " create_time        DATETIME)";
        String existSql = "select count(*)  from sqlite_master where type='table' and name = 'perf4j'";

        String tagIndex = "CREATE INDEX perf4j_tag_index ON perf4j (tag)";
        String timeIndex = "CREATE INDEX perf4j_time_index ON perf4j (create_time)";
        if (isExist(existSql)) {
            System.out.println("table perf4j is exist");
        } else {
            execute(createSql);
            execute(tagIndex);
            execute(timeIndex);
        }
    }

    /**
     * load jvm table
     *
     * @throws SQLException
     */
    public synchronized void loadJvm() throws SQLException {

        String createSql = "CREATE TABLE jvm " +
                "(tag            VARCHAR(255)     NOT NULL," +
                " count          varchar(100)    NOT NULL, " +
                " create_time        DATETIME)";
        String existSql = "select count(*)  from sqlite_master where type='table' and name = 'jvm'";

        String tagIndex = "CREATE INDEX jvm_tag_index ON jvm (tag)";
        String timeIndex = "CREATE INDEX jvm_time_index ON jvm (create_time)";

        if (isExist(existSql)) {
            System.out.println("table jvm is exist");
        } else {
            execute(createSql);
            execute(tagIndex);
            execute(timeIndex);
        }
    }

    /**
     * execute sql
     *
     * @param sql
     * @throws SQLException
     */
    public void execute(String sql) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        logger.info("executor sql {} ,result is {} ", sql, preparedStatement.execute());
        preparedStatement.close();
    }

    /**
     * return true if resultSet is not empty
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public boolean isExist(String sql) throws SQLException {
        boolean isExist = false;
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            if (resultSet.getInt(1) > 0)
                isExist = true;
            resultSet.close();
            preparedStatement.close();
        }
        return isExist;
    }

    /**
     * save GroupedTimingStatistics for perf4j
     *
     * @param groupedTimingStatistics
     * @throws SQLException
     */
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
            preparedStatement.setString(3, String.format("%.2f", timingStatistics.getMean()));
            preparedStatement.setDate(4, new Date(groupedTimingStatistics.getStopTime()));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    /**
     * save jvmGraphData for jvm
     *
     * @param jvmGraphDataList
     * @throws SQLException
     */
    public void save(List<JVMGraphData> jvmGraphDataList) throws SQLException {
        String sql = "INSERT INTO jvm (tag,`count`,create_time) " +
                "VALUES (?, ?, ?);";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        for (JVMGraphData jvmGraphData : jvmGraphDataList) {
            preparedStatement.setString(1, jvmGraphData.tag);
            preparedStatement.setString(2, jvmGraphData.count);
            preparedStatement.setDate(3, new Date(jvmGraphData.timestamp));
            preparedStatement.addBatch();
        }
        preparedStatement.executeBatch();
        preparedStatement.close();
    }

    /**
     * delete data before date
     *
     * @param date
     * @param table
     * @throws SQLException
     */
    public void deleteData(Date date, String table) throws SQLException {
        String sql = String.format("DELETE FROM  %s WHERE  `create_time` < ?", table);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, date);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public List<String> getAllTags(String table) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(String.format("SELECT DISTINCT (tag) from %s", table));
        List<String> tags = new ArrayList<String>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            tags.add(resultSet.getString(1));
        }
        resultSet.close();
        preparedStatement.close();
        return tags;
    }


    public String getDataFromPerf4j(Date from, Date to, String tag) throws SQLException {
        if (from == null) from = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 1);
        if (to == null) to = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        if (to.compareTo(DateUtils.getAddDayDate(from, 1)) > 0) {
            dateFormat=new SimpleDateFormat("MM/dd HH:mm:ss");
        }
        String sql = String.format("SELECT * from %s WHERE `tag` = ? AND `create_time` BETWEEN ? AND ?", perf4jTable);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, tag);
        preparedStatement.setDate(2, from);
        preparedStatement.setDate(3, to);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> labels = new LinkedList<String>();
        List<Integer> countDatas = new LinkedList<Integer>();
        List<String> meanDatas = new LinkedList<String>();
        List<String> tags = new LinkedList<String>();
        while (resultSet.next()) {
            Long label = resultSet.getLong(4);
            labels.add(dateFormat.format(new java.util.Date(label)));
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
        totalData.put("graph", array);
        totalData.put("tags", getAllTags(GraphDataTables.perf4jTable));
        totalData.put("hostname", NetHelper.getHostname());
        if (RequestScopeManage.pretty.get() != null && RequestScopeManage.pretty.get()) {
            // 格式化输出数据
            return JSON.toJSONString(totalData, SerializerFeature.PrettyFormat);
        } else {
            return JSON.toJSONString(totalData);
        }
    }

    public String getDataFromJvm(Date from, Date to) throws SQLException {
        if (from == null) from = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 1);
        if (to == null) to = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        if (to.compareTo(DateUtils.getAddDayDate(from, 1)) > 0) {
            dateFormat=new SimpleDateFormat("MM/dd HH:mm:ss");
        }
        String sql = String.format("SELECT * from %s WHERE `create_time` BETWEEN ? AND ?", jvmTable);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setDate(1, from);
        preparedStatement.setDate(2, to);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<JVMGraphData02> jvmGraphData02s = new ArrayList<JVMGraphData02>();
        JVMGraphData02 gcCounts = new JVMGraphData02(JVMInfo.GCCount);
        JVMGraphData02 gcTimes = new JVMGraphData02(JVMInfo.GCTime);
        JVMGraphData02 threads = new JVMGraphData02(JVMInfo.Thread);
        JVMGraphData02 usedHeaps = new JVMGraphData02(JVMInfo.UsedHeap);
        JVMGraphData02 usedNonHeaps = new JVMGraphData02(JVMInfo.UsedNonHeap);
        jvmGraphData02s.add(gcCounts);
        jvmGraphData02s.add(gcTimes);
        jvmGraphData02s.add(threads);
        jvmGraphData02s.add(usedHeaps);
        jvmGraphData02s.add(usedNonHeaps);

        while (resultSet.next()) {
            String tag = resultSet.getString(1);
            Long label = resultSet.getLong(3);
            String label01 = dateFormat.format(new java.util.Date(label));
            if (tag.equals(JVMInfo.GCCount)) {
                gcCounts.counts.add(resultSet.getString(2));
                gcCounts.labels.add(label01);
            } else if (JVMInfo.GCTime.equals(tag)) {
                gcTimes.counts.add(resultSet.getString(2));
                gcTimes.labels.add(label01);
            } else if (JVMInfo.Thread.equals(tag)) {
                threads.counts.add(resultSet.getString(2));
                threads.labels.add(label01);
            } else if (JVMInfo.UsedHeap.equals(tag)) {
                usedHeaps.counts.add(resultSet.getString(2));
                usedHeaps.labels.add(label01);
            } else if (JVMInfo.UsedNonHeap.equals(tag)) {
                usedNonHeaps.counts.add(resultSet.getString(2));
                usedNonHeaps.labels.add(label01);
            }
        }
        JSONObject object = new JSONObject();
        object.put("jvmData", jvmGraphData02s);
        object.put("hostname", new String(NetHelper.getHostname().getBytes(), Charset.forName("UTF-8")));
        if (RequestScopeManage.pretty.get() != null && RequestScopeManage.pretty.get()) {
            // 格式化输出数据
            return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
        } else {
            return JSON.toJSONString(object);
        }
    }


}
