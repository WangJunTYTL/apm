package com.peaceful.perf4j.demo.spring.webmvc;

import com.peaceful.common.util.Util;
import org.perf4j.LoggingStopWatch;
import org.perf4j.StopWatch;
import org.perf4j.aop.Profiled;
import org.perf4j.log4j.Log4JStopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
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

    @RequestMapping("demo")
    @ResponseBody
    public String stop() throws InterruptedException {
        // 根据自己项目用到的日志系统，用对应的stopwatch
        // 一个StopWatch代表一个计时器实例
        StopWatch stopWatch = new Slf4JStopWatch();
        stopWatch.stop("stop.a");
        Thread.sleep(500);
        stopWatch.stop("stop.a.b");
        Thread.sleep(500);
        stopWatch.stop("stop.a.b.c");
        Thread.sleep(500);
        stopWatch.stop("stop.d");
        Thread.sleep(500);
        stopWatch.stop("ooo");
        Thread.sleep(500);
        // 重新开始下一轮的计时
        stopWatch.lap("bbb");
        Thread.sleep(500);
        stopWatch.stop("ccc");
        return "perf4j test";
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
