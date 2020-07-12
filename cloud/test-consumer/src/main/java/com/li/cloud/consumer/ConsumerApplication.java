package com.li.cloud.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @desc 消费者服务 程序入口
 * @date 2020-05-13
 */
@SpringBootApplication
//@EnableDiscoveryClient // 将该有应用注册为eureka 客户端应用，开启服务发现
@SpringCloudApplication
@EnableFeignClients // 开启 spring cloud feign 支持
public class ConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

}
