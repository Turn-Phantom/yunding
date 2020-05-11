package com.yunding.server.queue;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @desc
 * @date 2020-04-08
 */
@SpringBootApplication(scanBasePackages="com.yunding.server")
@MapperScan({"com.yunding.server.common.basecurd.dao", "com.yunding.server.queue.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
public class QueueApplication {
    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class);
    }
}
