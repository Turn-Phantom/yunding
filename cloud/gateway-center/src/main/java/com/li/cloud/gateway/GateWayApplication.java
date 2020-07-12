package com.li.cloud.gateway;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * @desc 网关中心 启动入口
 * @date 2020-05-18
 */
@EnableZuulProxy // 开启Zuul的Api网关服务
@SpringBootApplication(scanBasePackages = {"com.li.cloud.gateway", "com.li.cloud.common"})
@MapperScan({"com.li.cloud.common.basecurd.dao", "com.li.cloud.gateway.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
public class GateWayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class, args);
    }
}
