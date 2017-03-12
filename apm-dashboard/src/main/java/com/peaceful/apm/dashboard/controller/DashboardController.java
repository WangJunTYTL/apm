package com.peaceful.apm.dashboard.controller;

import com.peaceful.apm.alert.helper.DateHelper;
import com.peaceful.apm.dashboard.datasource.MetricsSet;
import com.peaceful.apm.dashboard.helper.ResponseFormat;
import com.peaceful.apm.dashboard.service.DataSourceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/9/7.
 */
@Controller
@RequestMapping("dashboard")
public class DashboardController {


    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping("node/list")
    @ResponseBody
    @Description("select all node info")
    public ResponseFormat nodeListJson() {
        Map<String, List<String>> nodes = dataSourceService.getServiceInfo();
        if (nodes == null || nodes.isEmpty()) {
            return new ResponseFormat(1, "node list is empty!");
        }
        return new ResponseFormat(0, nodes);
    }

    @RequestMapping("service/node")
    @ResponseBody
    public ResponseFormat selectNodeByService(String service) {
        List<String> tags = dataSourceService.selectAllTag(service);
        return new ResponseFormat(0, tags);
    }


    @RequestMapping("")
    public String dashboard(String service, String node, String tag, String from, String to, Model model) {
        if (StringUtils.isEmpty(from)) {
            from = DateHelper.getStringByPattern(new Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000), "yyyy-MM-dd HH:mm");
        }
        model.addAttribute("from", from);
        if (StringUtils.isNoneBlank(to)) {
            model.addAttribute("to", to);
        }
        if (StringUtils.isBlank(node)) {
            List<String> nodes = dataSourceService.selectNodes(service);
            node = nodes.get(0);
        }
        List<String> tags = dataSourceService.selectTags(service, node);
        if (StringUtils.isEmpty(tag) && tags != null && !tags.isEmpty()) {
            tag = tags.get(0);
        }
        model.addAttribute("tag", tag);
        model.addAttribute("service", service);
        model.addAttribute("hostname", node);
        model.addAttribute("nodes", dataSourceService.selectNodes(service));
        model.addAttribute("tags", tags);
        return "dashboard";
    }

    @RequestMapping("/graph")
    @Description("获取图表结构的数据集")
    @ResponseBody
    public ResponseFormat selectByTag(String service, String hostname, String tag, String startTime, String endTime) {
        long start, end;
        if (StringUtils.isBlank(startTime)) {
            start = System.currentTimeMillis() - 2 * 60 * 60 * 1000;
        } else {
            start = DateHelper.getDateByPattern(startTime, "yyyy-MM-dd HH:mm").getTime();
        }
        if (StringUtils.isBlank(endTime)) {
            end = System.currentTimeMillis();
        } else {
            end = DateHelper.getDateByPattern(endTime, "yyyy-MM-dd HH:mm").getTime();
        }
        MetricsSet metricsSet = dataSourceService.selectByTag(service, hostname, start, end, tag);
        if (metricsSet == null) {
            return new ResponseFormat(1, "Not Fount Data");
        }
        return new ResponseFormat(0, metricsSet);
    }


}
