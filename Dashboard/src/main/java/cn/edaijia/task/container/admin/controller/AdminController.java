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
            // focused task data
            String focusedTaskData = TaskConsoleAPI.cat(TaskConsoleAPI.Method.focusedTaskBeanSet);
            // first flexible task data
            String firstFlexibleTaskData = TaskConsoleAPI.cat(TaskConsoleAPI.Method.firstFlexibleTaskBeanSet);
            // second flexible task data
            String secondFlexibleTaskBeanSet = TaskConsoleAPI.cat(TaskConsoleAPI.Method.secondFlexibleTaskBeanSet);
            // task container running info
            String runningInfo = TaskConsoleAPI.cat(TaskConsoleAPI.Method.runningInfo);
            logger.info("focusedTaskData {}", focusedTaskData);
            logger.info("firstFlexibleTaskData {}", firstFlexibleTaskData);
            request.setAttribute("focusedTaskData", JSON.parseObject(JSON.parseObject(focusedTaskData, String.class), List.class));
            request.setAttribute("firstFlexibleTaskData", JSON.parseObject(JSON.parseObject(firstFlexibleTaskData, String.class), List.class));
            request.setAttribute("secondFlexibleTaskBeanSet", JSON.parseObject(JSON.parseObject(secondFlexibleTaskBeanSet, String.class), List.class));
            request.setAttribute("runningInfo", JSON.parseObject(runningInfo, String.class));
        } catch (Exception e) {
            logger.error("request api error ", ExceptionUtils.getStackTrace(e));
        }
        return "welcome/index";
    }

}
