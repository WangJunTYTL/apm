package com.peaceful.apm.test.metric;

import com.peaceful.apm.test.message.StopWatchMessage;
import com.peaceful.apm.test.message.StopWatchMessageEvent;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wangjun38 on 2018/1/27.
 */
public class StopWatchParse {


    /**
     * The default regex used to parse StopWatches from Strings. The following is true of the
     * capturing groups of this pattern:
     * <ol>
     * <li> The start time in milliseconds, parseable as a long
     * <li> The elapsed time in milliseconds, parseable as a long
     * <li> The tag name
     * <li> Optional, if not null the message text.
     * </ol>
     */
    public static final String DEFAULT_MATCH_PATTERN =
            "start\\[(\\d+)\\] time\\[(\\d+)\\] tag\\[(.*?)\\] mark\\[(\\d+)\\](?: message\\[(.*?)\\])?";

    /**
     * The regex Pattern object used to parse Strings.
     */
    private Pattern pattern = Pattern.compile(DEFAULT_MATCH_PATTERN);


    public StopWatchMessageEvent parseStopWatch(String message) {
        MatchResult result = match(message);
        return (result != null) ? parseStopWatchFromLogMatch(result) : null;
    }

    /**
     * Gets the MatchResult object that is returned when the Pattern used by this parser matches the specified message.
     *
     * @param message The StopWatch message to parse.
     * @return The MatchResult from matching the message, or null if it didn't match.
     */
    public MatchResult match(String message) {
        Matcher matcher = pattern.matcher(message);
        return matcher.find() ? matcher.toMatchResult() : null;
    }

    /**
     * Helper method returns a new StopWatch from the MatchResult returned when a log messages matches.
     *
     * @param matchResult The regex match result
     * @return A new StopWatch that reflects the data from the match result.
     */
    public StopWatchMessageEvent parseStopWatchFromLogMatch(MatchResult matchResult) {
        StopWatchMessageEvent messageEvent = new StopWatchMessageEvent();
        messageEvent.setStart(Long.parseLong(matchResult.group(1)));
        messageEvent.setTime( Long.parseLong(matchResult.group(2)));
        messageEvent.setTag(matchResult.group(3));
        messageEvent.setWatchType(WatchType.findByTypeCode(Integer.parseInt(matchResult.group(4))));
        return messageEvent;

    }

    /**
     * This method is intended to be used when you want to do a quick check of whether or not the specified string
     * is valid WITHOUT incurring the cost to do a full parse. Thus, importantly, if this method returns false, the
     * message is GUARANTEED to NOT be parseable, BUT THE CONVERSE IS NOT TRUE. That is, if the method returns true,
     * you must still call one of the parse or match methods to determine if the message is in fact truly parseable.
     *
     * @param message The message to test
     * @return false if the message is DEFINITELY not parseable, true if it potentially (but not definitely) could
     *         be parsed.
     */
    public boolean isPotentiallyValid(String message) {
        return message.startsWith("start");
    }
}

