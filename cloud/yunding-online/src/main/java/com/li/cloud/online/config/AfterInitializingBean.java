package com.li.cloud.online.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    @Bean(name = "restTemplateForBalance")
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    /** 开启webSocket支持 */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
