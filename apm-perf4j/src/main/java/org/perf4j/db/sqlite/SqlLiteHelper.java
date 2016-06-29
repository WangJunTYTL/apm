package org.perf4j.db.sqlite;

import org.apache.commons.lang3.StringUtils;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.jvm.JVMGraphData;
import org.perf4j.jvm.JVMInfo;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 导入性能数据到sqlite中
 *
 * @author <a href="mailto:wangjuntytl@163.com">WangJun</a>
 * @version 1.0 15/12/10
 */
public class SqlLiteHelper {


    private static final int INIT_SUC = 1;
    private static final int NOT_INIT = 0;
    private static final int INIT_FAIL = -1;
    public static GraphDataTables graphDataTables;


    private static Connection connection = null;

    private static int state = NOT_INIT;

    private SqlLiteHelper() {

    }


    public static SqlLiteHelper getInstance(String path) throws Exception {
        if (state == INIT_SUC) {
            return Single.sqlLiteHelper;
        } else if (state == INIT_FAIL) {
            throw new Exception("can't init sqlite !");
        } else {
            try {
                SqlLiteHelper sqlLiteHelper = Single.sqlLiteHelper;
                sqlLiteHelper.initConn(path);
                graphDataTables = new GraphDataTables(connection);
                graphDataTables.loadPerf4j();
                graphDataTables.loadJvm();
                System.out.println("init sqlite suc ,path is " + path);
                state = INIT_SUC;
                return sqlLiteHelper;
            } catch (Exception e) {
                state = INIT_FAIL;
                throw e;
            }
        }
    }


    /**
     * 如果该对象已经实例化,则返回实例对象,否则返回null
     * Note: 如果对象还未实例化,该方法不会主动去初始化
     *
     * @return
     */
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
        if (StringUtils.isEmpty(path)) {
            throw new RuntimeException("You need config sqlite file path");
        }
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException(path + " is not exist,please create dir for " + path);
        }
        if (connection == null) {
            Class.forName("org.sqlite.JDBC");
            // 默认在当前目录下创建
            connection = DriverManager.getConnection("jdbc:sqlite:".concat(path));
            LoggerFactory.getLogger(getClass()).info("Opened database successfully");
        }
    }


    /**
     * save perf4j graph data
     *
     * @param groupedTimingStatistics
     * @throws SQLException
     */
    public void savePerf4jData(GroupedTimingStatistics groupedTimingStatistics) throws SQLException {
        graphDataTables.save(groupedTimingStatistics);
    }

    /**
     * save jvm graph data
     *
     * @throws SQLException
     */
    public void saveJVMData() throws SQLException {
        List<JVMGraphData> jvmGraphDataList = new ArrayList<JVMGraphData>();
        jvmGraphDataList.add(JVMInfo.getMemoryInfo(0));
        jvmGraphDataList.add(JVMInfo.getMemoryInfo(1));
        jvmGraphDataList.add(JVMInfo.getThreadInfo());
        jvmGraphDataList.add(JVMInfo.getGCInfo(0));
        jvmGraphDataList.add(JVMInfo.getGCInfo(1));
        graphDataTables.save(jvmGraphDataList);
    }

    public String getData(Date from, Date to, String tag, String table) throws SQLException {
        if (GraphDataTables.perf4jTable.equals(table)) {
            return graphDataTables.getDataFromPerf4j(from, to, tag);
        } else if (GraphDataTables.jvmTable.equals(table)) {
            return graphDataTables.getDataFromJvm(from, to);
        }
        return null;
    }

    /**
     * clear graph table data
     *
     * @param date
     * @throws SQLException
     */
    public void deleteData(Date date) throws SQLException {
        graphDataTables.deleteData(date, GraphDataTables.perf4jTable);
        graphDataTables.deleteData(date, GraphDataTables.jvmTable);
    }


}
