package com.peaceful.perf4j.dashboard.controller;

import com.peaceful.common.util.DateUtils;
import com.peaceful.common.util.Http;
import com.peaceful.perf4j.dashboard.common.TaskConsoleAPI;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author WangJun
 * @version 1.0 16/4/25
 */
@Controller
@RequestMapping("graph")
@ResponseBody
public class GraphDataController {

    @RequestMapping("/data")
    public void now(String method, String tag, String from, String to) {
        if (StringUtils.isNotEmpty(method)) {
            if (StringUtils.isEmpty(from)) from = "";
            else {
                Http.param("from", from);
                from = String.valueOf(DateUtils.getDateByPattern(from, "yyyy-MM-dd HH:mm").getTime());
            }
            if (StringUtils.isEmpty(to)) to = "";
            else {
                Http.param("to", to);
                to = String.valueOf(DateUtils.getDateByPattern(to, "yyyy-MM-dd HH:mm").getTime());
            }
            if (StringUtils.isEmpty(tag)) tag = "";
            else {
                Http.param("tag", tag);
            }
        } else {
            method = "";
        }
        String runningInfo = TaskConsoleAPI.cat(method + "&from=" + from + "&to=" + to + "&tag=" + tag);
        Http.responseJSON(runningInfo);
    }

}
