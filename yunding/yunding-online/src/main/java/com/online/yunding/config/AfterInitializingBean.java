package com.online.yunding.config;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @desc 初始化完成后执行
 * @date 2020-04-09
 */
@Configuration
public class AfterInitializingBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    /** 返回一个RestTemplate实例 */
    @Bean
    public RestTemplate getRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create()
                .setMaxConnTotal(3000) // 最大连接数
                .setMaxConnPerRoute(500) // 单个路由连接的最大数
                .build());
        httpRequestFactory.setConnectionRequestTimeout(10000);
        httpRequestFactory.setConnectTimeout(10000);
        httpRequestFactory.setReadTimeout(10000);
        return new RestTemplate(httpRequestFactory);
    }

    /** 开启webSocket支持 */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
