package com.yunding.server.tool;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @desc 启动类
 * @date 2020-05-06
 */
@SpringBootApplication
@MapperScan({"com.yunding.server.common.basecurd.dao", "com.yunding.server.tool.dao"}) // 注意：mapper扫描时，会将引入模块的service接口当做dao接口扫描
@ComponentScan(basePackages={"com.yunding.server.common.*","com.yunding.server.tool.*"}) // 指定组件扫描位置，若不指定则，默认扫描当前启动类同层以及子包，无法扫描公共类中的过滤器
public class ToolApplication {
    public static void main(String[] args) {
        SpringApplication.run(ToolApplication.class);
    }
}
