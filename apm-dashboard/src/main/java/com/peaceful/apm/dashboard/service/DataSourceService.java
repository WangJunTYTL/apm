package com.peaceful.apm.dashboard.service;

import com.google.common.collect.Lists;
import com.peaceful.apm.alert.helper.DateHelper;
import com.peaceful.apm.dashboard.datasource.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangjun on 16/9/8.
 */
@Service
public class DataSourceService {


    @Autowired
    private SqlSessionFactory sqlSessionFactory;
    private DecimalFormat df = new DecimalFormat("0.00");

    /**
     * 查询指定tag的数据,按创建时间进行排序
     *
     * @param service
     * @param hostname
     * @param startTime
     * @param endTime
     * @param tag
     * @return
     */
    public MetricsSet selectByTag(String service, String hostname, long startTime, long endTime, String tag) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            List<MetricsForTag> tagList = sqlSession.getMapper(DataSourceDao.class).selectData(service, hostname, tag, startTime, endTime);
            if (tagList == null || tagList.isEmpty()) {
                return null;
            }
            MetricsForTag metrics = tagList.get(0);
            MetricsSet metricsSet = new MetricsSet(metrics.tag, metrics.interval, metrics.hostname);
            for (MetricsForTag t : tagList) {
                metricsSet.tpss.add(df.format((float) t.count / t.interval));
                metricsSet.means.add(t.mean);
                metricsSet.mins.add(t.min);
                metricsSet.maxs.add(t.max);
                metricsSet.counts.add(t.count);
                metricsSet.stds.add(t.std);
                if ((endTime - startTime) / 1000 > 24 * 60 * 60) {
                    metricsSet.series.add(DateHelper.getStringByPattern(new Date(t.createTime), "MM-dd HH:mm:ss"));
                } else {
                    metricsSet.series.add(DateHelper.getStringByPattern(new Date(t.createTime), "HH:mm:ss"));
                }

            }
            return metricsSet;
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询最近两天所有的tag
     *
     * @param service
     * @param hostname
     * @return
     */
    public List<String> selectTags(String service, String hostname) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAllTag(service, hostname, DateHelper.getAddDayDate(new Date(), -1).getTime());
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查询指定服务注册的节点
     *
     * @param service
     * @return
     */
    public List<String> selectNodes(String service) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectNodes(service);
        } finally {
            sqlSession.close();
        }
    }

    public Map<String, List<String>> getServiceInfo() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            List<Node> nodes = sqlSession.getMapper(DataSourceDao.class).selectAllNode();
            Map<String, List<String>> nodeMap = new HashMap<>();
            for (Node node : nodes) {
                if (nodeMap.containsKey(node.service)) {
                    nodeMap.get(node.service).add(node.hostname);
                } else {
                    nodeMap.put(node.service, Lists.newArrayList(node.hostname));
                }
            }
            return nodeMap;
        } finally {
            sqlSession.close();
        }
    }

    public List<String> selectAllService() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAllService();
        } finally {
            sqlSession.close();
        }
    }

    public List<Node> selectAllNode() {

        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAllNode();
        } finally {
            sqlSession.close();
        }
    }

    /**
     * 查看最近一天指定服务的所有tag
     *
     * @param service
     * @return
     */
    public List<String> selectAllTag(String service) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAllTagByService(service, (System.currentTimeMillis() / 1000 - 24 * 60 * 60) * 1000);
        } finally {
            sqlSession.close();
        }
    }

    public int insertAlert(String service, String tag, String term, int interval, String smsMsg, String mailMsg, String receivers) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            AlertBuild alertBuild = sqlSession.getMapper(DataSourceDao.class).selectAlertByServiceAndTag(service, tag);
            if (alertBuild != null) {
                return 0;
            }
            return sqlSession.getMapper(DataSourceDao.class).insertAlert(service, tag, term, interval, smsMsg, mailMsg, receivers);
        } finally {
            sqlSession.close();
        }
    }

    public int updateAlert(Long id, String term, String smsMsg, String mailMsg, int interval, String receivers) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).updateAlert(term, interval, smsMsg, mailMsg, receivers, id);
        } finally {
            sqlSession.close();
        }
    }

    public List<AlertBuild> selectAllAlert() {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAllAlert();
        } finally {
            sqlSession.close();
        }
    }

    public AlertBuild selectAlertById(long id) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).selectAlertById(id);
        } finally {
            sqlSession.close();
        }
    }

    public int updateAlertStatusById(int status,long id) {
        SqlSession sqlSession = sqlSessionFactory.openSession(true);
        try {
            return sqlSession.getMapper(DataSourceDao.class).updateAlertStatus(status,id);
        } finally {
            sqlSession.close();
        }
    }

}
