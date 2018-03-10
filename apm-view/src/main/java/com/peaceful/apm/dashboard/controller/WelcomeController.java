package com.peaceful.apm.dashboard.controller;

import com.peaceful.apm.dashboard.service.DataSourceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wangjun38 on 2017/9/23.
 */
@RequestMapping("")
@Controller
public class WelcomeController {

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping({"/", "index"})
    public String index( Model model) {
        model.addAttribute("active","Monitor");
        model.addAttribute("services", dataSourceService.selectAllService());
        return "monitor";
    }
}
