package com.li.cloud.consumer.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @desc 自定义初始化bean
 * @date 2020-05-18
 */
@Configuration
public class CusInitializeBean {

    /** 创建restTemplate实例 */
    @Bean
    // 开启客户端负载均衡
    RestTemplate restTemplate(){
        return new RestTemplate();
    }

    /** 开启feign调用日志；  */
    /* 级别：
        NONE: 不记录任何信息
        BASIC: 仅记录请求方法，url， 状态码，响应时间
        HEADERS：包含BASIC信息；请求头响应头信息
        FULL：所有请求、响应明细信息
    */
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.BASIC;
    }
}
