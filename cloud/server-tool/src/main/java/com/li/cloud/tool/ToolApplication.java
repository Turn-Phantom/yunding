package com.li.cloud.tool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @desc 工具服务 启动入口
 * @date 2020-05-12
 */
@SpringBootApplication(scanBasePackages={"com.li.cloud.tool", "com.li.cloud.common"})
@MapperScan({"com.li.cloud.common.basecurd.dao", "com.li.cloud.tool.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
@EnableDiscoveryClient // 开启DiscoveryClient （服务发现）；能激活 Eureka 中的 DiscoveryClient 实现
public class ToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class, args);
    }
}
