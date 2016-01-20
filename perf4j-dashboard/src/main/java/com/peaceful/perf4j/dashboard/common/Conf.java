package com.peaceful.perf4j.dashboard.common;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * container cluster list conf and web front-end conf
 *
 * @author WangJun <wangjuntytl@163.com>
 * @version 1.0 15/9/24
 * @since 1.6
 */

public class Conf {

    public Map<String, String> clusterMap = new HashMap<String, String>();

    private static Config config;

    private static ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);


    private Conf() {
        config = ConfigFactory.load("ServerCluster");
        executorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                config = ConfigFactory.load("ServerCluster");
                // cluster list
                ConfigList list = config.getList("ServerCluster.clusterList");
                synchronized (clusterMap) {
                    clusterMap.clear();
                    for (ConfigValue configValue : list) {
                        Config config1 = configValue.atPath("cluster").getConfig("cluster");
                        clusterMap.put(config1.getString("name"), config1.getString("ip") + ":" + config1.getString("port") + config1.getString("url"));
                    }
                }
            }
        }, 0, 8, TimeUnit.SECONDS);

    }

    private static class Single {
        public static Conf conf = new Conf();
    }

    public static Conf getConf() {
        return Single.conf;
    }


}
