package com.li.cloud.tool.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @desc
 * @date 2020-06-11
 */
@Component
public class AfterInitializing implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }
}
