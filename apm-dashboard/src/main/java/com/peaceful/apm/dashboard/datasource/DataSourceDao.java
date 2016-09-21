package com.peaceful.apm.dashboard.datasource;

import com.peaceful.apm.dashboard.service.AlertBuild;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/9/8.
 */
public interface DataSourceDao {

    @Select("select distinct(tag) from APM_STATISTICS_${service} where hostname=#{hostname} and create_time >#{create_time} order by tag")
    List<String> selectAllTag(@Param("service") String service, @Param("hostname") String hostname, @Param("create_time") long createTime);

    @Select("select distinct(tag) from APM_STATISTICS_${service} where create_time >#{create_time} ")
    List<String> selectAllTagByService(@Param("service") String service, @Param("create_time") long createTime);

    @Select("select * from APM_SERVICE")
    List<Node> selectAllNode();

    @Select("select distinct(service) from APM_SERVICE")
    List<String> selectAllService();

    @Select("select `hostname` from APM_SERVICE where `service`=#{service}")
    List<String> selectNodes(@Param("service") String service);

    @Select("select * from APM_STATISTICS_${service} where tag in (#{tags}) and hostname = #{hostname} and  create_time between #{start_time} and #{end_time} order by create_time asc")
    List<MetricsForTag> selectData(@Param("service") String service, @Param("hostname") String hostname, @Param("tags") String tags, @Param("start_time") long startTime, @Param("end_time") long endTime);

    @Insert("insert into APM_ALERT (`service`,`tag`,`term`,`interval`,`sms_msg`,`mail_msg`,`receiver_groups`,`create_time`,`update_time`) values (#{service},#{tag},#{term},#{interval},#{sms_msg},#{mail_msg},#{receiver_groups},now(),now())")
    int insertAlert(@Param("service") String service, @Param("tag") String tag, @Param("term") String term, @Param("interval") int interval, @Param("sms_msg") String smsMsg, @Param("mail_msg") String mailMsg, @Param("receiver_groups") String receivers);

    @Update("update APM_ALERT set `interval`=#{interval} ,`term`=#{term},`sms_msg`=#{sms_msg},`mail_msg`=#{mail_msg},`receiver_groups`=#{receiver_groups} ,`update_time`=now() where id = #{id}")
    int updateAlert(@Param("term") String term, @Param("interval") int interval, @Param("sms_msg") String smsMsg, @Param("mail_msg") String mailMsg, @Param("receiver_groups") String receivers, @Param("id") long id);

    @Update("update APM_ALERT set `status`=#{status}  where id = #{id}")
    int updateAlertStatus( @Param("status") int status, @Param("id") long id);

    @Select("select * from APM_ALERT")
    List<AlertBuild> selectAllAlert();

    @Select("select * from APM_ALERT where id = #{id}")
    AlertBuild selectAlertById(@Param("id") Long id);

    @Select("select * from APM_ALERT where `service` = #{service} and `tag` = #{tag}")
    AlertBuild selectAlertByServiceAndTag(@Param("service") String service,@Param("tag")String tag);

    @Select("select round(sum(`count`),2) as count,round(avg(`mean`),2) as mean,max(`max`) as max,min(`min`) as min,`interval` from APM_STATISTICS_${service} where tag = #{tag} and create_time > #{create_time}")
    Map selectAvgData(@Param("service") String service, @Param("tag") String tag, @Param("create_time") long startTime);

}
