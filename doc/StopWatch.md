# StopWatch

一个StopWatch代表一个计时器，简单的使用方式

    StopWatch stopWatch = new StopWatch(); // 创建StopWatch就代表开始计时器开始计时
    try {
        ...code being timed...
        log.info(stopWatch.stop("methodBeingTimed.success")); // stop 就代表计时停止，并打一个tag，就像生活中真正使用到的计时器
    } catch (Exception e) {
        log.error(stopWatch.stop("methodBeingTimed.fail"), e);
    }

它内部维护的状态有
    
    // 计时器启动时间
    private long startTime;
    private long nanoStartTime;
    // 计时器已经运行时间
    private long elapsedTime;
    private String tag;
    private String message;    

其主要统计的结果输出如下

    public String toString() {
        String message = getMessage();
        return "start[" + getStartTime() +
           "] time[" + getElapsedTime() +
           "] tag[" + getTag() +
           ((message == null) ? "]" : "] message[" + message + "]");
    }

然后根据这份输出日志进行分析
    

在实际业务中使用方式
    
    // 根据自己项目用到的日志系统，用对应的stopwatch，最好用slf4jStopwatch,比如slf4j提供log4j但没提供logback的实现，这时如果用的slf作为日志门面便可解决logback的问题
    // 一个StopWatch代表一个计时器实例，
    StopWatch stopWatch = new Slf4JStopWatch();
    stopWatch.stop("stop.a");
    Thread.sleep(500);
    stopWatch.stop("stop.a.b");
    Thread.sleep(500);
    stopWatch.stop("stop.a.b.c");
    Thread.sleep(500);
    stopWatch.stop("stop.d");
    Thread.sleep(500);
    stopWatch.stop("ooo");
    Thread.sleep(500);
    // 重新开始下一轮的计时，记录bbb标记时，时从创建计时器开始到bbb，记录ccc是从bbb开始计时到ccc结束
    stopWatch.lap("bbb");
    Thread.sleep(500);
    stopWatch.stop("ccc");     
    
    