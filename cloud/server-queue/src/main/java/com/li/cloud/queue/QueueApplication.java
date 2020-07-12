package com.li.cloud.queue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @desc
 * @date 2020-04-08
 */
@SpringBootApplication(scanBasePackages={"com.li.cloud.common", "com.li.cloud.queue"})
@MapperScan({"com.li.cloud.common.basecurd.dao", "com.li.cloud.queue.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
@EnableDiscoveryClient
public class QueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class);
    }
}
