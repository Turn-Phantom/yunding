package com.online.yunding;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @desc
 * @date 2020-03-21
 */
@SpringBootApplication(scanBasePackages="com.online.yunding")
@MapperScan({"com.online.yunding.common.basecurd.dao", "com.online.yunding.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
public class YundingApplication {
    public static void main(String[] args) {
        SpringApplication.run(YundingApplication.class, args);
    }

}

