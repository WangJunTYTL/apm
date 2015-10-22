# LoggingStopWatch

    // --- Template Methods ---
    /**
     * This log method can be overridden by subclasses in order to persist the StopWatch, for example by using a
     * log4j Logger. The default implementation here just writes the StopWatch to the standard error stream.
     *
     * @param stopWatchAsString The serialized StopWatch string
     * @param exception         An exception, if any, that was also passed to the stop() or lap() methods - may be null.
     */
    protected void log(String stopWatchAsString, Throwable exception) {
        System.err.println(stopWatchAsString);
        if (exception != null) {
            exception.printStackTrace();
        }
    }
    // --- Object Methods ---
    public LoggingStopWatch clone() {
        return (LoggingStopWatch) super.clone();
    }
    // --- Private Methods ---
    // Helper method only calls log if elapsed time is greater than the time threshold
    private void doLogInternal(String stopWatchAsString, Throwable exception) {
    	//if normalAndSlowSuffixesEnabled then always log with the suffixes added
    	//getTag() should take care of appending the correct tag, and should already be part of stopWatchAsString
        //Otherwise we default to the backward-compatible behavior: namely:
    	//in most cases timeThreshold will be 0, so just short circuit out as fast as possible
    	long elapsedTime = getElapsedTime(); // to allow for subclasses to override this value
    	long timeThreshold = getTimeThreshold(); // to allow for subclasses to override this value
    	if (timeThreshold == 0 || isNormalAndSlowSuffixesEnabled() || elapsedTime >= timeThreshold) {
            log(stopWatchAsString, exception);