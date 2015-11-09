package org.perf4j.chart;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.perf4j.GroupedTimingStatistics;
import org.perf4j.TimingStatistics;
import org.perf4j.helpers.StatsValueRetriever;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/7/29
 * @since 1.6
 */

public class BaiduEChartGenerator implements StatisticsChartGenerator {


    private LinkedList<GroupedTimingStatistics> data = new LinkedList<GroupedTimingStatistics>();
    private int maxDataPoints = DEFAULT_MAX_DATA_POINTS;
    private StatsValueRetriever valueRetriever;
    private Set<String> enabledTags = null;


    public BaiduEChartGenerator(StatsValueRetriever statsValueRetriever) {
        this.valueRetriever = statsValueRetriever;
    }

    public String getChartUrl() {
        return parseData();
    }

    public void appendData(GroupedTimingStatistics statistics) {
        if (this.data.size() >= this.maxDataPoints) {
            this.data.removeFirst();
        }
        this.data.add(statistics);
    }

    public List<GroupedTimingStatistics> getData() {
        return Collections.unmodifiableList(this.data);
    }

    // List<GroupedTimingStatistics>  解析成图表控件可以识别的数据结构
    public String parseData() {
        String graphType = valueRetriever.getValueName();
        long minTimeValue = Long.MAX_VALUE;
        long maxTimeValue = Long.MIN_VALUE;
        double maxDataValue = Double.MIN_VALUE;


        //set up the axis labels - we use the US decimal format locale to ensure the decimal separator is . and not ,
        DecimalFormat decimalFormat = new DecimalFormat("##0.0", new DecimalFormatSymbols(Locale.US));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat.setTimeZone(GroupedTimingStatistics.getTimeZone());

        List<String> labels = new ArrayList<String>();
        List<Long> labels2 = new ArrayList<Long>();
        List<String> positions = new ArrayList<String>();
        List<String> tags = new ArrayList<String>();

        //labels so the chart doesn't get too crowded
//        int stepSize = this.data.size() / this.maxDataPoints + 1;
        int stepSize = 1;


        for (Iterator<GroupedTimingStatistics> iter = data.iterator(); iter.hasNext(); ) {
            GroupedTimingStatistics groupedTimingStatistics = iter.next();
            long windowStartTime = groupedTimingStatistics.getStartTime();
            String label = dateFormat.format(new Date(windowStartTime));
            double position = 100.0 * (windowStartTime - minTimeValue) / (maxTimeValue - minTimeValue);
            labels.add(label);
            labels2.add(windowStartTime);
            positions.add(decimalFormat.format(position));

            //skip over some windows if stepSize is greater than 1
            for (int i = 1; i < stepSize && iter.hasNext(); i++) {
                iter.next();
            }
        }


        //{"firstBlock":[[1438740120000,1438740180000],[0.1,0.16666666666666666]],"queue_pop":[[1438740120000,1438740180000],[2.0166666666666666,2]]}
        Map<String, List<Number>[]> tagsToXDataAndYData = new TreeMap<String, List<Number>[]>();


        for (GroupedTimingStatistics groupedTimingStatistics : data) {
            Map<String, TimingStatistics> statsByTag = groupedTimingStatistics.getStatisticsByTag();
            long windowStartTime = groupedTimingStatistics.getStartTime();
            long windowLength = groupedTimingStatistics.getStopTime() - windowStartTime;
            //keep track of the min/max time value, this is needed for scaling the chart parameters
            minTimeValue = Math.min(minTimeValue, windowStartTime);
            maxTimeValue = Math.max(maxTimeValue, windowStartTime);

            for (Map.Entry<String, TimingStatistics> tagWithData : statsByTag.entrySet()) {
                String tag = tagWithData.getKey();
                if (this.enabledTags == null || this.enabledTags.contains(tag)) {
                    //get the corresponding value from tagsToXDataAndYData
                    List<Number>[] xAndYData = tagsToXDataAndYData.get(tagWithData.getKey());
                    if (xAndYData == null) {
                        tagsToXDataAndYData.put(tag, xAndYData = new List[]{new ArrayList<Number>(),
                                new ArrayList<Number>()});
                    }

                    //the x data is the start time of the window, the y data is the value
                    Number yValue = this.valueRetriever.getStatsValue(tagWithData.getValue(), windowLength);
                    xAndYData[0].add(windowStartTime);
                    xAndYData[1].add(yValue.doubleValue());

                    //update the max data value, which is needed for scaling
                    maxDataValue = Math.max(maxDataValue, yValue.doubleValue());
                }
            }
        }

        Map<String, List<String>> tagsToYData = new TreeMap<String, List<String>>();
        Set<String> tagsData = tagsToXDataAndYData.keySet();
        for (String tag : tagsData) {
            tags.add(tag);
            List<String> tagYData = tagsToYData.get(tag);
            if (tagYData == null) {
                tagsToYData.put(tag, tagYData = new ArrayList<String>());
            }
            List<Number>[] XYData = tagsToXDataAndYData.get(tag);
            List<Number> XData = XYData[0];
            List<Number> YData = XYData[1];
            int n = 0;
            for (int i = 0; i < labels2.size() && i < XData.size(); i++) {
                long xi = XData.get(i).longValue();
                if (labels2.get(i) <= xi && xi < labels2.get((i+1))) {
                    tagYData.add(String.format("%.2f", YData.get(i).doubleValue()));
                } else {
                    tagYData.add("0.00");
                }
            }
        }


        //if it's empty, there's nothing to display
        if (tagsToXDataAndYData.isEmpty()) {
            return "";
        }


        JSONObject data = new JSONObject();
        data.put("graphType", graphType);
        data.put("tags", tags);
        data.put("labels", labels);
        data.put("tagsToYData", tagsToYData);
        data.put("tagsToXDataAndYData", tagsToXDataAndYData);
//        data.put("positions", positions);

        return JSON.toJSONString(data);
    }

    /**
     * Gets the set of tag names for which values will be displayed on the chart. Each tag is represented as a
     * separate series on the chart.
     *
     * @return The set of enabled tag names, or null if ALL tags found in the GroupedTimingStatistics data will be
     * displayed.
     */
    public Set<String> getEnabledTags() {
        return enabledTags;
    }

    /**
     * Sets the set of tag names for which values will be displayed on the chart.
     *
     * @param enabledTags The set of enabled tag names. If this method is not called, or if enabledTags is null,
     *                    then ALL tags from the GroupedTimingStatistics data will be displayed on the chart.
     */
    public void setEnabledTags(Set<String> enabledTags) {
        this.enabledTags = enabledTags;
    }
}
