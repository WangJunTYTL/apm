package com.peaceful.apm.alert.helper;

import com.google.common.base.Preconditions;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 快速集成启动
 *
 * @author WangJun
 * @version 1.0 16/6/18
 */
public class AlertApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(AlertApplication.class);
    private static Config config;
    private static String startDate = DateHelper.formatDateTime(new Date());
    private static final String ENV_KEY = "running.env";

    static {
        // load config file ，default file
        config = ConfigFactory.load("peaceful-boot-reference.properties");
        if (AlertApplication.class.getClassLoader().getResource("application.properties") != null) {
            // merge config file
            Config _config = ConfigFactory.load("application.properties");
            config = _config.withFallback(config);
        }
        String runningEnv = config.getString(ENV_KEY);
        // check running.env state
        Preconditions.checkState((runningEnv.equals("dev") || runningEnv.equals("test") || runningEnv.equals("product")), "[running.env=%s] is a illegal state", runningEnv);
//        Console.log("running.env: %s", runningEnv);
    }

    public static void loadToJVM() {
        // nothing
    }

    /**
     * 获取配置信息
     */
    public static Config getConfigContext() {
        return config;
    }

    /**
     * 是否在dev环境运行
     */
    public static boolean isDev() {
        if (getConfigContext().getString(ENV_KEY).equals("dev")) {
            return true;
        }
        return false;
    }

    /**
     * 是否在test环境运行
     */
    public static boolean isTest() {
        if (getConfigContext().getString(ENV_KEY).equals("test")) {
            return true;
        }
        return false;
    }

    /**
     * 是否在product环境运行
     */
    public static boolean isProduct() {
        if (getConfigContext().getString(ENV_KEY).equals("product")) {
            return true;
        }
        return false;
    }

    /**
     * 返回当前运行环境
     */
    public static String getRunningEnv() {
        return getConfigContext().getString(ENV_KEY);
    }

    /**
     * 返回启动时间
     */
    public static String getStartDate() {
        return startDate;
    }

}
