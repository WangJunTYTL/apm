package org.perf4j.monitor;

import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Random;

public class PerformanceInterceptor extends HandlerInterceptorAdapter {

    protected org.slf4j.Logger logger = LoggerFactory
            .getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {
        StopWatch stopWatch = new Log4JStopWatch();
        request.setAttribute("stopWatch", stopWatch);
        stopWatch.lap("http_request");
        String reqURI = request.getRequestURI();
        stopWatch.start(reqURI);
        //response.addHeader("Access-Control-Allow-Origin", "*");
        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
            HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        StopWatch stopWatch = (StopWatch) request.getAttribute("stopWatch");
        long spentTime = stopWatch.getElapsedTime();
        String reqURI = request.getRequestURI();
        stopWatch.stop(reqURI);
        stopWatch.lap("http_request_end");
        if (spentTime < 1000) {
            // pass
        } else if (spentTime >= 20000) {
            logger.error("[SLOW_S] [{}] spentTime is {}", reqURI,
                    String.valueOf(spentTime));
        } else if (spentTime >= 10000 & spentTime < 20000) {
            logger.warn("[SLOW_10_20] [{}]  spentTime is {}", reqURI,
                    String.valueOf(spentTime));
        } else if (spentTime >= 5000) {
            logger.warn("[SLOW_5_10] [{}] spentTime is {}", reqURI,
                    String.valueOf(spentTime));
        } else if (spentTime >= 1000) {
            logger.warn("[SLOW_1_5] [{}] spentTime is {}", reqURI,
                    String.valueOf(spentTime));
        }
        if (spentTime > 1000){
//            FatalErrorMonitor.putError(reqURI, "响应时间在1s以上");
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
