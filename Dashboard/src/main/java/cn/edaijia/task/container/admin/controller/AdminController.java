package cn.edaijia.task.container.admin.controller;

import cn.edaijia.task.container.common.TaskConsoleAPI;
import com.alibaba.fastjson.JSON;
import com.peaceful.common.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Display data provided by the task console api
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/15
 * @since 1.6
 */
@Controller
public class AdminController {


    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping({"index", "welcome", "/"})
    public String welcome(HttpServletRequest request) {
        try {
            // runningInfo = data
            String runningInfo = TaskConsoleAPI.cat(TaskConsoleAPI.Method.runningInfo);
            logger.info("runningInfo {}", runningInfo);
            request.setAttribute("runningInfo",runningInfo);
        } catch (Exception e) {
            logger.error("request api error ", ExceptionUtils.getStackTrace(e));
        }
        return "welcome/index";
    }

}
