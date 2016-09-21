# GroupedTimingStatistics

GroupedTimingStatistics对象是存放所有分析结果`TimingStatistics`的容器，它把分析好的`TimingStatistics`放入到一个SortMap中

    private SortedMap<String, TimingStatistics> statisticsByTag = new TreeMap<String, TimingStatistics>();


## 触发TimingStatistics的分析
    
    private void addStopWatchToStatsByTag(String tag, StopWatch stopWatch) {
        TimingStatistics stats = statisticsByTag.get(tag);
        if (stats == null) {
            statisticsByTag.put(tag, stats = new TimingStatistics());
        }
        stats.addSampleTime(stopWatch.getElapsedTime());
    }
    
    
## 分析结果输出样式    
    
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        
        int paddingToAllowForLongestTag = Math.max(getLongestTag(statisticsByTag.keySet()), "Tag".length());
        
        //output the time window
        retVal.append("Performance Statistics   ")
                .append(MiscUtils.formatDateIso8601(startTime))
                .append(" - ")
                .append(MiscUtils.formatDateIso8601(stopTime))
                .append(MiscUtils.NEWLINE);
        //output the header
        retVal.append(String.format("%-" + paddingToAllowForLongestTag + "s%12s%12s%12s%12s%12s%12s%n",
                                    "Tag", "Avg(ms)", "Min", "Max", "Std-Dev", "Count", "Total"));
        //output each statistics
        for (Map.Entry<String, TimingStatistics> tagWithTimingStatistics : statisticsByTag.entrySet()) {
            String tag = tagWithTimingStatistics.getKey();
            TimingStatistics timingStatistics = tagWithTimingStatistics.getValue();
            double totalTimeForTag = timingStatistics.getCount() * timingStatistics.getMean();
            retVal.append(String.format("%-" + paddingToAllowForLongestTag + "s%12.1f%12d%12d%12.1f%12d%12.0f%n",
                                        tag,
                                        timingStatistics.getMean(),
                                        timingStatistics.getMin(),
                                        timingStatistics.getMax(),
                                        timingStatistics.getStandardDeviation(),
                                        timingStatistics.getCount(),
                                        totalTimeForTag));
        }
        return retVal.toString();
    }    