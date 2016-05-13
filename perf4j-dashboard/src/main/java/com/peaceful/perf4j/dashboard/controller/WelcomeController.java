package com.peaceful.perf4j.dashboard.controller;

import com.peaceful.common.util.DateUtils;
import com.peaceful.perf4j.dashboard.common.TaskConsoleAPI;
import com.peaceful.common.util.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Display data provided by the task console api
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */
@Controller
public class WelcomeController {


    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping({"index", "welcome", "/"})
    public String welcome(HttpServletRequest request) {
        String method = "";
        String from = "";
        String to = "";
        String tag = "";
        try {
            method = request.getParameter("method");
            if (StringUtils.isNotEmpty(method)) {
                tag = request.getParameter("tag");
                from = request.getParameter("from");
                to = request.getParameter("to");
                if (StringUtils.isEmpty(from)) from = "";
                else {
                    request.setAttribute("from", from);
                    from = String.valueOf(DateUtils.getDateByPattern(from, "yyyy-MM-dd HH:mm").getTime());
                }
                if (StringUtils.isEmpty(to)) to = "";
                else {
                    request.setAttribute("to", to);
                    to = String.valueOf(DateUtils.getDateByPattern(to, "yyyy-MM-dd HH:mm").getTime());
                }
                if (StringUtils.isEmpty(tag)) tag = "";
                else {
                    request.setAttribute("tag", tag);
                }
                String runningInfo = TaskConsoleAPI.cat(method + "&from=" + from + "&to=" + to + "&tag=" + tag);
                logger.info("runningInfo {}", runningInfo);
                request.setAttribute("runningInfo", runningInfo);
            }
        } catch (Exception e) {
            logger.error("request api error ", ExceptionUtils.getStackTrace(e));
        }
        if ("history".equals(method)) {
            return "welcome/history";
        } else if ("jvm".equals(method)) {
            return "welcome/jvm";
        } else if ("now".equals(method)) {
            return "welcome/index";
        }
        return "welcome/nodes";
    }

    @RequestMapping("profile")
    public String profile() {
        return "welcome/profile";
    }


}
