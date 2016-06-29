package org.perf4j.servlet;

import org.apache.commons.lang3.StringUtils;
import org.perf4j.chart.StatisticsChartGenerator;
import org.perf4j.db.sqlite.GraphDataTables;
import org.perf4j.helpers.MiscUtils;
import org.perf4j.log4j.GraphingStatisticsAppender;
import org.perf4j.db.sqlite.SqlLiteHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * 用于暴漏性能数据接口
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/10/17
 * @since 1.6
 */

public class VitaGraphingServlet extends HttpServlet {

    private static final long serialVersionUID = 7877125631806499443L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // method方法对应着数据类型：now 、history 、 jvm
        String method = request.getParameter("method");
        if (request.getParameterMap().containsKey("pretty")) {
            RequestScopeManage.pretty.set(true);
        }else {
            RequestScopeManage.pretty.set(false);
        }
        // 默认只显示实时的数据
        if (StringUtils.isBlank(method) || method.equals("now")) {
            // 这里的数据是从内存中直接获取，响应会比较快速
            // 这里是获取指定的图表
            Map<String, StatisticsChartGenerator> chartsByName = getChartGeneratorsToDisplay(request);
            response.setContentType("application/json;charset=utf-8");
            writeHeader(request, response);
            response.getWriter().println("[");
            int x = 0;
            for (Map.Entry<String, StatisticsChartGenerator> nameAndChart : chartsByName.entrySet()) {
                String chartUrl = nameAndChart.getValue().getChartUrl();
                if (StringUtils.isBlank(chartUrl)) continue;
                x++;
                if (x == 1) {
                    // pass
                } else {
                    response.getWriter().println(",");
                }
                response.getWriter().println(chartUrl);
            }
            response.getWriter().println("]");
        } else {
            // 下面是从本地文件中查询得到的数据
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            String tag = request.getParameter("tag");
            int _dataType = 0;
            if (method.equals("jvm")) {
                _dataType = 1; // jvm
            } else {
                _dataType = 0; // history
            }
            response.getWriter().println(getMetaData(from, to, tag, _dataType));
        }
        writeHeader(request, response);
    }

    private String getMetaData(String from, String to, String tag, int type) {
        SqlLiteHelper sqlLiteHelper = SqlLiteHelper.getInstance();
        if (sqlLiteHelper == null) {
            return null;
        } else {
            try {
                java.sql.Date _from = null;
                java.sql.Date _to = null;
                if (StringUtils.isEmpty(from)) from = null;
                else _from = new Date(Long.valueOf(from));
                if (StringUtils.isEmpty(to)) to = null;
                else _to = new Date(Long.valueOf(to));
                if (type == 0) {
                    if (StringUtils.isEmpty(tag)) {
                        List<String> tags = sqlLiteHelper.graphDataTables.getAllTags(GraphDataTables.perf4jTable);
                        if (tags.size() > 0) tag = tags.get(0);
                        else {
                            return null;
                        }
                    }
                    return sqlLiteHelper.getData(_from, _to, tag, GraphDataTables.perf4jTable);
                } else {
                    return sqlLiteHelper.getData(_from, _to, tag, GraphDataTables.jvmTable);

                }
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Helper method writes the HTML header, everything up to the opening body tag. Subclasses may wish to override.
     */
    protected void writeHeader(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Helper method writes the HTML footer, closing the body and HTML tags. Subclasses may wish to override.
     */
    protected void writeFooter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().flush();
    }

    /**
     * Helper method generates the list of charts that are to be displayed in this call to the servlet. In general
     * subclasses should not need to override this method.
     *
     * @param request The incoming request, which may contain a list of "graphName" parameters, in which case those
     *                graphs will be displayed
     * @return A map of graph name to the chart generator capable of creating the URL for the graph.
     */
    protected Map<String, StatisticsChartGenerator> getChartGeneratorsToDisplay(HttpServletRequest request) {

        // find the names of the graphs to be displayed
        List<String> graphsToDisplay;

        if (request.getParameter("graphName") != null) {
            // option 1 - passed in on the request
            graphsToDisplay = Arrays.asList(request.getParameterValues("graphName"));
        } else {
            // option 3 - no graphs specified, return all known graphs
            graphsToDisplay = getAllKnownGraphNames();
        }

        Map<String, StatisticsChartGenerator> retVal = new LinkedHashMap<String, StatisticsChartGenerator>();
        for (String graphName : graphsToDisplay) {
            retVal.put(graphName, getGraphByName(graphName));
        }
        return retVal;
    }

    /**
     * This method looks for all known GraphingStatisticsAppenders and returns their names.
     *
     * @return The list of known GraphingStatisticsAppender names.
     */
    protected List<String> getAllKnownGraphNames() {
        List<String> retVal = new ArrayList<String>();
        for (GraphingStatisticsAppender appender : GraphingStatisticsAppender.getAllGraphingStatisticsAppenders()) {
            retVal.add(appender.getName());
        }
        return retVal;
    }

    /**
     * Finds the specified graph by using the
     * {@link GraphingStatisticsAppender#getAppenderByName(String)} method to find the appender with
     * the specified name.
     *
     * @param name the name of the GraphingStatisticsAppender whose chart generator should be returned.
     * @return The specified chart generator, or null if no appender with the specified name was found.
     */
    protected StatisticsChartGenerator getGraphByName(String name) {
        GraphingStatisticsAppender appender = GraphingStatisticsAppender.getAppenderByName(name);
        return (appender == null) ? null : appender.getChartGenerator();
    }
}
