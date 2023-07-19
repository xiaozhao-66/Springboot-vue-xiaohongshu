package com.yanhuo.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

/**
 * @author: lockie
 * @Date: 2019/11/27 16:43
 * @Description: es配置
 */
@Configuration
public class EsClientBuilders {
    private static final Logger logger = LoggerFactory.getLogger(EsClientBuilders.class);

    @Value("${es.url}")
    String esUrl;
    @Value("${es.port}")
    int esPort;

    // 设置连接超时时间
    private final int connectTimeOut = 2000;
    private final int socketTimeOut = 30000;
    private final int connectionRequestTimeOut = 10000;
    // 一次最多接收请求
    private final int maxConnectNum = 100;
    // 某一个服务每次能并行接收的请求数量
    private final int maxConnectPerRoute = 100;
    private RestClientBuilder builder;
    private RestHighLevelClient client;

    /**
     * 初始化连接
     *
     * @return
     */
    @Bean(name = "restHighLevelClient")
    public RestHighLevelClient getClientBuilder() {
        String schema = "http";
        HttpHost httpHost = new HttpHost(esUrl, esPort, schema);
        builder = RestClient.builder(httpHost);
        // 设置连接时间
        boolean uniqueConnectTimeConfig = true;
        if (uniqueConnectTimeConfig) {
            setConnnectTimeOutConfig();
        }
        // 设置连接数
        boolean uniqueConnectNumConfig = true;
        if (uniqueConnectNumConfig) {
            setMutiConnectConfig();
        }

        client = new RestHighLevelClient(builder);
        return client;
    }

    /**
     * 异步httpclient的连接延迟配置
     * 设置修改默认请求配置的回调（例如：请求超时，认证，或者其他设置
     */
    public void setConnnectTimeOutConfig() {
        builder.setRequestConfigCallback(builder -> {
            builder.setConnectTimeout(connectTimeOut);
            builder.setSocketTimeout(socketTimeOut);
            builder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return builder;
        });
    }

    /**
     * 线程设置
     */
    public void setMutiConnectConfig() {
        builder.setHttpClientConfigCallback(httpAsyncClientBuilder -> {
            httpAsyncClientBuilder.setMaxConnTotal(maxConnectNum);
            httpAsyncClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpAsyncClientBuilder;
        });
    }

    /**
     * 关闭es连接
     */
    public void close() {
        if (client != null) {
            try {
                client.close();
            } catch (IOException e) {
                logger.error("关闭es连接异常");
            }
        }
    }
}
