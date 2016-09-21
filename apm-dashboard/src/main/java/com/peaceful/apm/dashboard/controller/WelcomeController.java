package com.peaceful.apm.dashboard.controller;

import com.peaceful.apm.dashboard.service.DataSourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

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
    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping({"index", "welcome", "/"})
    public String welcome(Model model) {
        Map<String, List<String>> nodeMap = dataSourceService.getServiceInfo();
        model.addAttribute("serviceInfo", nodeMap);
        return "index";
    }

}
