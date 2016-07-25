package com.peaceful.apm.aggregate.servlet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.apm.aggregate.LogbackGraphingStatisticsAppender;
import com.peaceful.apm.aggregate.MetricsFromJdbc;
import com.peaceful.apm.aggregate.MetricsSet;
import com.peaceful.boot.common.helper.NetHelper;
import org.apache.commons.lang3.StringUtils;
import org.perf4j.chart.StatisticsChartGenerator;
import org.perf4j.servlet.AbstractGraphingServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 这里通过servlet暴漏性能数据，数据的结构是结合百度Echart的图表渲染结构组装
 * 可以在这里查询实时的监控数据、也可以查询历史的监控数据
 *
 * @author WangJun
 * @version 1.0 16/7/13
 */
public class LogbackBaiduEchartGraphingServlet extends AbstractGraphingServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // method：now 、history 、 jvm
        String method = request.getParameter("method");
        if (request.getParameterMap().containsKey("pretty")) {
            RequestScopeManage.pretty.set(true);
        } else {
            RequestScopeManage.pretty.set(false);
        }
        // 默认只显示实时的数据
        if (StringUtils.isBlank(method) || method.equals("now")) {
            // 这里的数据是从内存中直接获取，响应会比较快速
            Map<String, StatisticsChartGenerator> chartsByName = getChartGeneratorsToDisplay(request);
            response.setContentType("application/json;charset=utf-8");
            writeHeader(request, response);
            response.getWriter().print("[");
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
            response.getWriter().print("]");
        } else {
            // 下面是从本地文件中查询得到的数据
            String from = request.getParameter("from");
            String to = request.getParameter("to");
            String tag = request.getParameter("tag");
            if (method.equals("jvm")) {
                response.getWriter().println(getFromJdbc(from, to, tag, DataType.Jvm));
            } else {
                response.getWriter().println(getFromJdbc(from, to, tag, DataType.History));
            }
        }
        writeHeader(request, response);
    }

    private enum DataType {
        History, Jvm
    }

    private String getFromJdbc(String from, String to, String tag, DataType dataType) {
        Long _from, _to;
        if (StringUtils.isBlank(from))
            _from = System.currentTimeMillis() - 1000 * 60 * 60 * 2;
        else
            _from = Long.valueOf(from);
        if (StringUtils.isBlank(to))
            _to = System.currentTimeMillis();
        else
            _to = Long.valueOf(to);
        if (dataType == DataType.Jvm) {
            return getJVM(_from, _to);
        }

        if (StringUtils.isBlank(tag)) {
            List<String> tags = MetricsFromJdbc.selectMetricsTag();
            if (tags != null && !tags.isEmpty())
                tag = tags.get(0);
        }
        return getHistory(tag, _from, _to);
    }

    private String getHistory(String tag, Long from, Long to) {
        JSONObject totalData = new JSONObject();
        MetricsSet metricsSet = MetricsFromJdbc.selectMetrics(tag, Long.valueOf(from), Long.valueOf(to));
        totalData.put("allTags", MetricsFromJdbc.selectMetricsTag());
        totalData.put("currentTag", tag);
        totalData.put("hostname", NetHelper.getHostname());
        if (metricsSet == null) {
            return totalData.toString();
        }

        JSONArray array = new JSONArray();
        JSONObject countData = new JSONObject();
        JSONObject meanData = new JSONObject();
        JSONObject minData = new JSONObject();
        JSONObject maxData = new JSONObject();
        JSONObject stdData = new JSONObject();

        countData.put("graphType", "Count");
        List<Double> tps = new LinkedList<>();
        for (Long c : metricsSet.counts) {
            BigDecimal bg = new BigDecimal(c / Float.valueOf(metricsSet.interval));
            double r = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            tps.add(r);
        }
        countData.put("tagsToYData", tps);

        meanData.put("graphType", "Mean");
        meanData.put("tagsToYData", metricsSet.means);

        minData.put("graphType", "Min");
        minData.put("tagsToYData", metricsSet.mins);

        maxData.put("graphType", "Max");
        maxData.put("tagsToYData", metricsSet.maxs);

        stdData.put("graphType", "Std");
        stdData.put("tagsToYData", metricsSet.stds);

        array.add(countData);
        array.add(meanData);
        array.add(minData);
        array.add(maxData);
        array.add(stdData);
        totalData.put("graph", array);


        totalData.put("label", metricsSet.series);
        if (RequestScopeManage.pretty.get() != null && RequestScopeManage.pretty.get()) {
            // format json data
            return JSON.toJSONString(totalData, SerializerFeature.PrettyFormat);
        } else {
            return JSON.toJSONString(totalData);
        }
    }

    private String getJVM(Long from, Long to) {
        MetricsSet set = MetricsFromJdbc.selectMetrics("ThreadCount", from, to);
        JSONObject data = new JSONObject();
        if (set != null) {
            data.put("label", set.series);
            data.put("thread.count", set.counts);
        }
        set = MetricsFromJdbc.selectMetrics("GC", from, to);
        if (set != null) {
            data.put("gc.count", set.counts);
            data.put("gc.time", set.means);
        }
        set = MetricsFromJdbc.selectMetrics("UsedHeap", from, to);
        if (set != null) {
            data.put("used.heap.size", set.counts);
        }
        set = MetricsFromJdbc.selectMetrics("UsedNonHeap", from, to);
        if (set != null) {
            data.put("used.nonHeap.size", set.counts);
        }
        data.put("hostname", new String(NetHelper.getHostname().getBytes(), Charset.forName("UTF-8")));
        if (RequestScopeManage.pretty.get() != null && RequestScopeManage.pretty.get()) {
            // format json data
            return JSON.toJSONString(data, SerializerFeature.PrettyFormat);
        } else {
            return JSON.toJSONString(data);
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
     * Finds the specified graph by using the
     * {@link org.perf4j.logback.GraphingStatisticsAppender#getAppenderByName(String)} method to find the appender with
     * the specified name.
     *
     * @param name the name of the GraphingStatisticsAppender whose chart generator should be returned.
     * @return The specified chart generator, or null if no appender with the specified name was found.
     */
    protected StatisticsChartGenerator getGraphByName(String name) {
        LogbackGraphingStatisticsAppender appender = LogbackGraphingStatisticsAppender.getAppenderByName(name);

        return (appender == null) ? null : appender.getChartGenerator();
    }

    /**
     * This method looks for all known GraphingStatisticsAppenders and returns their names.
     *
     * @return The list of known GraphingStatisticsAppender names.
     */
    protected List<String> getAllKnownGraphNames() {
        List<String> retVal = new ArrayList<String>();

        for (LogbackGraphingStatisticsAppender appender : LogbackGraphingStatisticsAppender.getAllGraphingStatisticsAppenders()) {
            retVal.add(appender.getName());
        }

        return retVal;
    }

}
