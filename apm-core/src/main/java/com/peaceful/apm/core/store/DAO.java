package com.peaceful.apm.core.store;

/**
 * Created by wangjun38 on 2017/9/22.
 */
public class DAO {


    public static String createApmHeartbeatTable(int routeId) {
        String heartbeatTable = "apm_statistics_" + routeId;
        String apm_service_heartbeat = "CREATE TABLE IF NOT EXISTS " + heartbeatTable + "(" +
                "  `hostname` VARCHAR(100) DEFAULT 'Unknown'," +
                "  `tag` VARCHAR(200)," +
                "  `metric` VARCHAR(200)," +
                "  `value` BIGINT," +
                "  `create_time` BIGINT       NOT NULL," +
                "  INDEX (tag)," +
                "  INDEX (hostname)," +
                "  INDEX (create_time)" +
                ")ENGINE=MyISAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";
        return apm_service_heartbeat;
    }

    public static String createApmServiceTable() {
        String apm_service = "CREATE TABLE IF NOT EXISTS apm_service (" +
                "  `service`  VARCHAR(50) NOT NULL ," +
                "  `hostname` VARCHAR(100)," +
                "   PRIMARY KEY (`service`,`hostname`) " +
                ")ENGINE=MyISAM DEFAULT CHARSET utf8 COLLATE utf8_general_ci;";
        return apm_service;
    }


}
