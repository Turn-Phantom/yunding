package com.li.cloud.video;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @desc
 * @date 2020-04-08
 */
@SpringBootApplication(scanBasePackages={"com.li.cloud.common", "com.li.cloud.video"})
@MapperScan({"com.li.cloud.common.basecurd.dao", "com.li.cloud.video.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
@EnableDiscoveryClient // 开启DiscoveryClient （服务发现）；能激活 Eureka 中的 DiscoveryClient 实现
public class VideoApplication {
    public static void main(String[] args) {
        SpringApplication.run(VideoApplication.class);
    }
}
