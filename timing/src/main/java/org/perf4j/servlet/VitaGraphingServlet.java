package org.perf4j.servlet;

import org.perf4j.chart.StatisticsChartGenerator;
import org.perf4j.helpers.MiscUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/10/17
 * @since 1.6
 */

public abstract class VitaGraphingServlet extends HttpServlet{

    private static final long serialVersionUID = 7877125631806499443L;

    /**
     * Setting an init parameter "graphNames" to a comma-separated list of the names of graphs to display by default
     * sets this member variable. Subclass implementations determine how graphs are named. For example, the
     * {@link org.perf4j.log4j.servlet.GraphingServlet} uses the names of
     * {@link org.perf4j.log4j.GraphingStatisticsAppender}s to determine which graphs to show.
     */
    protected List<String> graphNames;

    public void init() throws ServletException {
        String graphNamesString = getInitParameter("graphNames");
        if (graphNamesString != null) {
            graphNames = Arrays.asList(MiscUtils.splitAndTrim(graphNamesString, ","));
        }
    }

    public void destroy() {
        graphNames = null;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Map<String, StatisticsChartGenerator> chartsByName = getChartGeneratorsToDisplay(request);

        response.setContentType("text/html;charset=utf-8");

        writeHeader(request, response);
        for (Map.Entry<String, StatisticsChartGenerator> nameAndChart : chartsByName.entrySet()) {
            writeChart(nameAndChart.getKey(), nameAndChart.getValue(), request, response);
        }
        writeFooter(request, response);
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
     * Helper method writes the chart to the page using an img tag. Subclasses may wish to override.
     *
     * @param name           the name of the chart to write
     * @param chartGenerator the chart generator responsible for creating the chart URL
     * @param request        the incoming servlet request
     * @param response       the servlet respone
     */
    protected void writeChart(String name,
                              StatisticsChartGenerator chartGenerator,
                              HttpServletRequest request,
                              HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().println("<br><br>");

        String chartUrl = (chartGenerator == null) ? null : chartGenerator.getChartUrl();
        if (chartUrl != null) {
            response.getWriter().println("<b>" + name + "</b><br>");
            response.getWriter().println("<div class=\"chart\">" + chartUrl + "</div>");
        } else {
            response.getWriter().println("<b>Unknown graph name: " + name + "</b><br>");
        }
    }

    /**
     * Helper method writes the HTML footer, closing the body and HTML tags. Subclasses may wish to override.
     */
    protected void writeFooter(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().println("<script type='text/javascript'>");
        response.getWriter().println("$(function () {\n" +
                "\n" +
                "    var charts = $(\".chart\");\n" +
                "    for (var n = 0; n < charts.length; n++) {\n" +
                "        var c = charts[n];\n" +
                "        var config = $(c).html();\n" +
                "        $(c).html(\"\");\n" +
                "        var node = $(\"<div>\").attr(\"id\", \"chart\" + n).attr(\"style\", \"height:500px;\");\n" +
                "        $(c).append(node);\n" +
                "        //alert(config)\n" +
                "        if (config == null || config == \"\")\n" +
                "            continue;\n" +
                "        var parseData = JSON.parse(config);\n" +
                "        var myChart = echarts.init(document.getElementById(\"chart\" + n));\n" +
                "        var series = []\n" +
                "        for (var tag in parseData.tagsToYData) {\n" +
                "            var tagData = {\n" +
                "                name: tag,\n" +
                "                type: 'line',\n" +
                "                data: parseData.tagsToYData[tag],\n" +
                "                markPoint: {\n" +
                "                    data: [\n" +
                "                        {type: 'max', name: '最大值'},\n" +
                "                        //{type : 'min', name: '最小值'}\n" +
                "                    ]\n" +
                "                },\n" +
                "                markLine: {\n" +
                "                    data: [\n" +
                "                        {type: 'average', name: '平均值'}\n" +
                "                    ]\n" +
                "                }\n" +
                "            }\n" +
                "            series.push(tagData)\n" +
                "        }\n" +
                "        var option = {\n" +
                "            title: {\n" +
                "                //text: parseData.graphType,\n" +
                "                //subtext: '纯属虚构'\n" +
                "            },\n" +
                "            tooltip: {\n" +
                "                trigger: 'axis'\n" +
                "            },\n" +
                "            legend: {\n" +
                "                data: parseData.tags,\n" +
                "            },\n" +
                "            toolbox: {\n" +
                "                show: true,\n" +
                "                feature: {\n" +
                "                    mark: {show: false},\n" +
                "                    dataView: {show: true, readOnly: false},\n" +
                "                    magicType: {show: true, type: ['line', 'bar']},\n" +
                "                    restore: {show: true},\n" +
                "                    saveAsImage: {show: true}\n" +
                "                }\n" +
                "            },\n" +
                "            calculable: true,\n" +
                "            xAxis: [\n" +
                "                {\n" +
                "                    type: 'category',\n" +
                "                    boundaryGap: false,\n" +
                "                    data: parseData.labels\n" +
                "                }\n" +
                "            ],\n" +
                "            yAxis: [\n" +
                "                {\n" +
                "                    type: 'value',\n" +
                "                    axisLabel: {\n" +
                "                        formatter: '{value} '\n" +
                "                    }\n" +
                "                }\n" +
                "            ],\n" +
                "            series: series\n" +
                "        };\n" +
                "\n" +
                "\n" +
                "        // 为echarts对象加载数据\n" +
                "        myChart.setOption(option);\n" +
                "    }\n" +
                "\n" +
                "});\n");
        response.getWriter().println("</script>");
        response.getWriter().println("</body>");
        response.getWriter().println("</html>");
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
        } else if (graphNames != null) {
            // option 2 - specified as an init parameter
            graphsToDisplay = graphNames;
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
     * Subclasses should implement this method to return a chart generator by its name. Subclasses may use any method
     * necessary to find the underlying repository of charts.
     *
     * @param name the name of the graph to return
     * @return the chart generator capable of creating the requested chart.
     */
    protected abstract StatisticsChartGenerator getGraphByName(String name);

    /**
     * Subclasses should implement this method to return a list of all possible known graph names.
     *
     * @return The list of possible graph names for which <tt>getGraphByName</tt> will return a valid chart generator.
     */
    protected abstract List<String> getAllKnownGraphNames();
}
