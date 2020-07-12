package com.li.cloud.third;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @desc 第三方接口管理服务入口
 * @date 2020-06-21
 */
@SpringBootApplication(scanBasePackages = {"com.li.cloud.third", "com.li.cloud.common"})
@MapperScan({"com.li.cloud.common.basecurd.dao", "com.li.cloud.third.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
@EnableDiscoveryClient // 开启DiscoveryClient （服务发现）；能激活 Eureka 中的 DiscoveryClient 实现
public class ThirdApplication {
    public static void main(String[] args) {
        SpringApplication.run(ThirdApplication.class, args);
    }
}
