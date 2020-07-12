package com.li.cloud.registry;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @desc 注册中心； 启动入口
 * @date 2020-05-11
 */
@SpringBootApplication
@EnableEurekaServer // 开启注册服务中心
public class RegistryCenterApplication {
    public static void main(String[] args) {
        /*
            默认为NONE
            WebApplicationType.SERVLET 启动服务的环境<springMVC环境>；
            WebApplicationType.REACTIVE 启动服务 WebFlux 服务环境
         */
        new SpringApplicationBuilder(RegistryCenterApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}
