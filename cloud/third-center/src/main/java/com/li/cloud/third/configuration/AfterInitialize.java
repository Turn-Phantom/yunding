package com.li.cloud.third.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @desc 初始化完成后执行
 * @date 2020-06-21
 */
@Component
public class AfterInitialize implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {

    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
