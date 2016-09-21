# TimingStatistics

TimingStatistics是用于性能分析的对象，它依据StopWatch计时器对像进行分析，主要分析指标有

    private double mean;
    private double runningQ; //for keeping running standard deviation
    private long max;
    private long min;
    private int count;
    

它分析的方式也很简单，通过下面这个方法    
    
    public TimingStatistics addSampleTime(long elapsedTime) {
        count++;
        double diffFromMean = elapsedTime - mean;
        mean = mean + (diffFromMean / count);
        runningQ = runningQ + (((count - 1) * Math.pow(diffFromMean, 2.0)) / count);
        //special case initial stopWatch when finding max and min
        if (count == 1) {
            min = elapsedTime;
            max = elapsedTime;
        } else {
            if (elapsedTime < min) {
                min = elapsedTime;
            }
            if (elapsedTime > max) {
                max = elapsedTime;
            }
        }
        return this;
    }
    
    