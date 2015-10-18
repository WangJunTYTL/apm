package com.peaceful.demo.spring.webmvc;

import com.peaceful.common.util.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Date 14/11/1.
 * Author WangJun
 * Email wangjuntytl@163.com
 */
@Controller
public class TestController {

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("WARN:service will stop!!!");
            }
        }));
    }

    @RequestMapping({"test", ""})
    @ResponseBody
    public String test() {
        Util.report("hello world");
        return "hello world";
    }

    @RequestMapping("jsp")
    public String testJsp() {
        return "test";
    }

    @RequestMapping("ftl")
    public String testFtl() {
        return "test.ftl";
    }

}
