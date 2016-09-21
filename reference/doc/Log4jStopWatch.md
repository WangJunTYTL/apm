# Log4jStopWatch

perf4j的StopWatch在去收集记录这些性能数据然后交给TimingStatistics分析，最后还需要把分析好的性能数据或者收集的数据交给具体的日志系统，这样用户就可以从日志组件考虑怎么处理这份数据。

## perf4j 支持的日志组件

1. Log4jStopWatch
1. Slf4jStopWatch
1. JavaLogStopWatch
1. CommonsLogStopWatch


## 对接方式

     /**
     * Creates a Log4JStopWatch with the tag specified, no message and started at the instant of creation. The Logger
     * with the name "org.perf4j.TimingLogger" is used to log stop watch messages at the INFO level, or at the WARN
     * level if an exception is passed to one of the stop or lap methods.
     *
     * @param tag The tag name for this timing call. Tags are used to group timing logs, thus each block
     *            of code being timed should have a unique tag. Note that tags can take a hierarchical
     *            format using dot notation.
     */
    public Log4JStopWatch(String tag) {
        this(tag, null, Logger.getLogger(DEFAULT_LOGGER_NAME), Level.INFO, Level.WARN);
    }
    
    /**
     * The log message is overridden to use the log4j Logger to persist the stop watch.
     *
     * @param stopWatchAsString The stringified view of the stop watch for logging.
     * @param exception         An exception, if any, that was passed to the stop or lap method. If this is null then
     *                          logging will occur at normalPriority, if non-null it will occur at exceptionPriority.
     */
    protected void log(String stopWatchAsString, Throwable exception) {
        logger.log((exception == null) ? normalPriority : exceptionPriority, stopWatchAsString, exception);
    }
