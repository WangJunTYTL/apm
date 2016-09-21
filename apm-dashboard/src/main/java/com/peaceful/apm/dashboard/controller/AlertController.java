package com.peaceful.apm.dashboard.controller;

import com.alibaba.fastjson.JSON;
import com.peaceful.apm.alert.msgbox.MailMsg;
import com.peaceful.apm.alert.msgbox.SmsMsg;
import com.peaceful.apm.alert.users.User;
import com.peaceful.apm.dashboard.service.AlertBuild;
import com.peaceful.apm.dashboard.helper.ResponseFormat;
import com.peaceful.apm.dashboard.service.DataSourceService;
import com.peaceful.apm.dashboard.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by wangjun on 16/9/14.
 */
@Controller
@RequestMapping("alert")
public class AlertController {

    @Autowired
    private DataSourceService dataSourceService;
    @Autowired
    private UserService userService;

    @RequestMapping("list")
    public String alertList(Model model) {
        List<AlertBuild> alertBuildList = dataSourceService.selectAllAlert();
        model.addAttribute("alertList", alertBuildList);
        return "alert/list";
    }

    @RequestMapping("build")
    public String alertNew(Model model) {
        List<String> allService = dataSourceService.selectAllService();
        model.addAttribute("serviceList", allService);
        model.addAttribute("userGroups", userService.selectAllUserGroups());
        return "alert/build";
    }

    @RequestMapping("update/status")
    @ResponseBody
    public ResponseFormat alertUpdateStaus(int status, Long id) {
        dataSourceService.updateAlertStatusById(status, id);
        return new ResponseFormat(0, "OK");
    }

    @RequestMapping("build/update")
    public String alertUpdate(Model model, Long id) {
        AlertBuild alertBuild = dataSourceService.selectAlertById(id);
        if (StringUtils.isNoneBlank(alertBuild.getMailMsg())) {
            MailMsg msg = JSON.parseObject(alertBuild.getMailMsg(), MailMsg.class);
            alertBuild.setMailMsg(msg.content);
            model.addAttribute("subject", msg.subject);
        }
        if (StringUtils.isNoneBlank(alertBuild.getSmsMsg())) {
            SmsMsg msg = JSON.parseObject(alertBuild.getSmsMsg(), SmsMsg.class);
            alertBuild.setSmsMsg(msg.content);
        }
        List<Long> ids = JSON.parseArray(alertBuild.getReceiverGroups(),Long.class);
        model.addAttribute("userGroups", userService.selectAllUserGroups());
        model.addAttribute("alertBuild", alertBuild);
        model.addAttribute("checkedUserGroup", ids);
        return "alert/update";
    }

    @RequestMapping(value = "build/post", method = RequestMethod.POST)
    @ResponseBody
    @Description("build new alert")
    public ResponseFormat alertBuildPost(Long id, String service, String tag, String term, Integer interval, String smsMsg, String mailMsg, String subject, HttpServletRequest request) {
        String[] receivers = request.getParameterValues("receivers[]");
        if (StringUtils.isBlank(service)) {
            return new ResponseFormat(1, "Service is Empty");
        }
        if (StringUtils.isBlank(tag)) {
            return new ResponseFormat(1, "Tag is Empty");
        }
        if (StringUtils.isBlank(term)) {
            return new ResponseFormat(1, "Term Expression is Empty");
        }
        if (receivers == null || receivers.length == 0) {
            return new ResponseFormat(1, "Receiver Groups is Empty");
        }
        if (StringUtils.isBlank(smsMsg) && StringUtils.isBlank(mailMsg)) {
            return new ResponseFormat(1, "Message is Empty");
        }
        if (StringUtils.isNoneBlank(smsMsg)) {
            SmsMsg sms = new SmsMsg();
            sms.content = smsMsg;
            smsMsg = JSON.toJSONString(sms);
        }
        if (StringUtils.isNoneBlank(mailMsg)) {
            MailMsg mail = new MailMsg();
            mail.content = mailMsg;
            mail.subject = subject;
            mailMsg = JSON.toJSONString(mail);
        }
        if (id != null && id > 0) {
            dataSourceService.updateAlert(id, term, smsMsg, mailMsg, interval, JSON.toJSONString(receivers));
        } else {
            int res = dataSourceService.insertAlert(service, tag, term, interval, smsMsg, mailMsg, JSON.toJSONString(receivers));
            if (res == 0) {
                return new ResponseFormat(0, "Service:" + service + " Tag:" + tag + " is Exist");
            }
        }
        return new ResponseFormat(0, "OK");
    }

}
