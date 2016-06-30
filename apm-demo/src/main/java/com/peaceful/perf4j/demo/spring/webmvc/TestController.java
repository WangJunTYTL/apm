package com.peaceful.perf4j.demo.spring.webmvc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.peaceful.apm.aggregate.MetricsForTag;
import com.peaceful.apm.aggregate.MetricsFromJdbc;
import com.peaceful.apm.aggregate.MetricsSet;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

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

    @RequestMapping(value = {"test", ""}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String test() throws InterruptedException {
        StopWatch stopWatch = new Slf4JStopWatch("welcome-page");
        StringBuffer buffer = new StringBuffer();
        buffer.append("Apm is a monitor for the business level!");
        buffer.append("\n");
        buffer.append("----------------------------------------------");
        buffer.append("\n");
        buffer.append("你可以不断的请求该地址，Apm会帮助你记录请求的地址、次数、与响应时间");
        buffer.append("\n");
        buffer.append("----------------------------------------------");
        buffer.append("\n");
        TimeUnit.SECONDS.sleep(1);
        stopWatch.stop();
        MetricsForTag metricsForTag = MetricsFromJdbc.selectOneMetrics("welcome-page", System.currentTimeMillis() - 2 * 60 * 1000, System.currentTimeMillis());
        if (metricsForTag != null) {
            buffer.append(JSON.toJSONString(metricsForTag, SerializerFeature.PrettyFormat));
            buffer.append("\n");
            buffer.append("接下来，你也可以启动dashboard以更直观的方式查看监控数据!");
        } else {
            buffer.append("请继续刷~~30s后就可以看到数据了");
        }
        return buffer.toString();
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
