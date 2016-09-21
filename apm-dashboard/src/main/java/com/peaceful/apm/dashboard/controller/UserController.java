package com.peaceful.apm.dashboard.controller;

import com.peaceful.apm.dashboard.helper.ResponseFormat;
import com.peaceful.apm.dashboard.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangjun on 16/9/19.
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("group")
    public ResponseFormat getUserGroups() {
        return new ResponseFormat(0, userService.selectAllUserGroups());
    }

}
