package com.peaceful.apm.test.appender;

import com.google.common.base.Throwables;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.sql.DataSource;

import lombok.Getter;
import lombok.Setter;

/**
 * 将消息写入到ElasticSearch，继承该类后可以直接通过getClient()方法获取可用连接
 *
 * 特别说明
 * ----------
 * ElasticSearch各版本之间是不兼容的，要求必须客户端版本和服务端版本是一致的，这里使用的版本是：2.4.6
 *
 * Created by wangjun38 on 2018/1/28.
 */
public abstract class ElasticSearch2MessageAppender implements MessageAppender {


    @Setter
    private String host;
    @Setter
    private int port = 9300;
    @Setter
    private String clusterName = "elasticsearch";


    private boolean isOK;
    private Client client;

    public ElasticSearch2MessageAppender(String host, int port, String clusterName) {
        this.host = host;
        this.port = port;
        this.clusterName = clusterName;

        Settings settings = Settings.settingsBuilder().put("cluster.name", clusterName).build();
        try {
            client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
        } catch (UnknownHostException e) {
            System.err.println("elasticsearch start error:" + Throwables.getStackTraceAsString(e));
        }
        isOK = true;
    }

    public Client getClient() {
        if (isOK) return client;
        throw new RuntimeException("elasticSearch client is disabled!");
    }
}
