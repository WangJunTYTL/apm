package com.peaceful.apm.plugin;

import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author WangJun
 * @version 1.0 16/6/26
 */
public class SpringMvcRequest extends HandlerInterceptorAdapter {

    protected org.slf4j.Logger logger = LoggerFactory
            .getLogger(this.getClass());

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        String reqURI = request.getRequestURI();
        if (reqURI.indexOf("css") == -1 && reqURI.indexOf("js") == -1 && reqURI.indexOf("image") == -1) {
            StopWatch stopWatch = new Slf4JStopWatch();
            request.setAttribute("stopWatch", stopWatch);
            stopWatch.lap("http.request");
        }
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
        if (stopWatch != null) {
            String reqURI = request.getRequestURI();
            if (reqURI.equals("/")) {
                stopWatch.stop("http.index");
            } else {
                reqURI = reqURI.replaceAll("/", ".");
                stopWatch.stop("http" + reqURI);
            }
            stopWatch.lap("http.request");
        }
        super.afterCompletion(request, response, handler, ex);
    }
}
