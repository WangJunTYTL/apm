package com.peaceful.apm.dashboard.datasource;

import com.peaceful.apm.dashboard.service.AlertBuild;

import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/9/8.
 */
public interface DataSourceDao {

    @Select("select distinct(metric) from apm_statistics_${service} where hostname=#{hostname} and create_time >#{create_time} order by metric")
    List<String> selectAllTag(@Param("service") int routeId, @Param("hostname") String hostname, @Param("create_time") long createTime);

    @Select("select distinct(metric) from apm_statistics_${tableId} where create_time >#{create_time} ")
    List<String> selectAllTagByService(@Param("tableId") int tableId, @Param("create_time") long createTime);

    @Select("select distinct(tag) from apm_statistics_${tableId} where create_time >#{create_time} ")
    List<String> selectAllHeartBeatTypeByService(@Param("tableId") int tableId, @Param("create_time") long createTime);

    @Select("select * from apm_service")
    List<Node> selectAllNode();

    @Select("select id from apm_service_route where service = #{service}")
    int selectRouteTableId(@Param("service") String service);

    @Select("select distinct(service) from apm_service_route")
    List<String> selectAllService();

    @Select("select `hostname` from apm_service where `service`=#{service}")
    List<String> selectNodes(@Param("service") String service);

    @Select("select * from apm_statistics_${routeId} where metric in (#{tags}) and hostname = #{hostname} and  create_time between #{start_time} and #{end_time} order by create_time asc")
    List<MetricsForTag> selectData(@Param("routeId") int routeId, @Param("hostname") String hostname, @Param("tags") String tags, @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Select("select metric,value,create_time from apm_statistics_${routeId} where tag = #{tag} and hostname = #{hostname} and  create_time between #{start_time} and #{end_time} order by create_time asc")
    List<MetricsForHeartbeat> selectHeartbeatData(@Param("routeId") int routeId, @Param("hostname") String hostname, @Param("tag") String tag, @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Insert("insert into apm_alert (`service`,`metric`,`term`,`interval`,`sms_msg`,`mail_msg`,`receiver_groups`,`create_time`,`update_time`) values (#{service},#{metric},#{term},#{interval},#{sms_msg},#{mail_msg},#{receiver_groups},now(),now())")
    int insertAlert(@Param("service") String service, @Param("metric") String tag, @Param("term") String term, @Param("interval") int interval, @Param("sms_msg") String smsMsg, @Param("mail_msg") String mailMsg, @Param("receiver_groups") String receivers);

    @Update("update apm_alert set `interval`=#{interval} ,`term`=#{term},`sms_msg`=#{sms_msg},`mail_msg`=#{mail_msg},`receiver_groups`=#{receiver_groups} ,`update_time`=now() where id = #{id}")
    int updateAlert(@Param("term") String term, @Param("interval") int interval, @Param("sms_msg") String smsMsg, @Param("mail_msg") String mailMsg, @Param("receiver_groups") String receivers, @Param("id") long id);

    @Update("update apm_alert set `status`=#{status}  where id = #{id}")
    int updateAlertStatus(@Param("status") int status, @Param("id") long id);

    @Select("select * from apm_alert where service = #{service}")
    List<AlertBuild> selectAllAlert(@Param("service") String service);

    @Select("select * from apm_alert where id = #{id}")
    AlertBuild selectAlertById(@Param("id") Long id);

    @Select("select * from apm_alert where `service` = #{service} and `metric` = #{metric}")
    AlertBuild selectAlertByServiceAndTag(@Param("service") String service, @Param("metric") String tag);

    @Select("select round(sum(`count`),2) as count,round(avg(`mean`),2) as mean,max(`max`) as max,min(`min`) as min,avg(`interval`) as `interval` from apm_statistics_#{service} where metric = #{metric} and create_time > #{create_time}")
    Map selectAvgData(@Param("service") int routeId, @Param("metric") String tag, @Param("create_time") long startTime);

}
