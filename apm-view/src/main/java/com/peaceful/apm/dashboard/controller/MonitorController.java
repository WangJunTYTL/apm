package com.peaceful.apm.dashboard.controller;

import com.google.common.collect.Maps;

import com.peaceful.apm.alert.helper.DateHelper;
import com.peaceful.apm.dashboard.datasource.MetricsSet;
import com.peaceful.apm.dashboard.datasource.MetricsSetForHeartBeat;
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
@RequestMapping("monitor")
public class MonitorController {


    @Autowired
    DataSourceService dataSourceService;


    @RequestMapping("node")
    @ResponseBody
    public ResponseFormat selectHeartbeatListByService(String service) {
        List<String> nodes = dataSourceService.selectNodes(service);
        List<String> types = dataSourceService.selectHeartbeatTypeList(service);
        Map<String, List<String>> data = Maps.newHashMap();
        data.put("nodes", nodes);
        data.put("types", types);
        return new ResponseFormat(0, data);
    }

    @RequestMapping({"", "index"})
    public String dashboard(String service, String node, String tag, String from, String to, Model model) {
        if (StringUtils.isEmpty(from)) {
            from = DateHelper.getStringByPattern(new Date(System.currentTimeMillis() - 1 * 60 * 60 * 1000), "yyyy-MM-dd HH:mm");
        }
        model.addAttribute("from", from);
        if (StringUtils.isNoneBlank(to)) {
            model.addAttribute("to", to);
        }
        model.addAttribute("tag", tag);
        model.addAttribute("service", service);
        model.addAttribute("node", node);
        model.addAttribute("services", dataSourceService.selectAllService());
        model.addAttribute("active", "Monitor");
        return "monitor";
    }

    @RequestMapping("/graph")
    @Description("获取图表结构的数据集")
    @ResponseBody
    public ResponseFormat selectByType(String service, String node, String type, String startTime, String endTime) {
        long start, end;
        if (StringUtils.isBlank(startTime)) {
            start = System.currentTimeMillis() / 1000 - 60 * 60;
        } else {
            start = DateHelper.getDateByPattern(startTime, "yyyy-MM-dd HH:mm").getTime() / 1000;
        }
        if (StringUtils.isBlank(endTime)) {
            end = System.currentTimeMillis() / 1000;
        } else {
            end = DateHelper.getDateByPattern(endTime, "yyyy-MM-dd HH:mm").getTime() / 1000;
        }
        if (start > System.currentTimeMillis() / 1000) {
            return new ResponseFormat(1, "开始时间不可以大于当前时间", null);
        }
        if (end < start) {
            return new ResponseFormat(1, "结束时间小于开始时间", null);
        }
        if (((end - start)) > 24 * 60 * 60) {
            return new ResponseFormat(1, "时间跨度不可以超过24小时", null);
        }
        MetricsSetForHeartBeat metricsSet = dataSourceService.selectByType(service, node, type, start, end);
        if (metricsSet == null) {
            return new ResponseFormat(2, "没有数据", null);
        }
        return new ResponseFormat(0, metricsSet);
    }


}
