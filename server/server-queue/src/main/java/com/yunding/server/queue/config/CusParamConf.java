package com.yunding.server.queue.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc
 * @date 2020-04-26
 */
@Configuration
@ConfigurationProperties(prefix = "yunding.server")
@Getter
@Setter
public class CusParamConf {

    /** 主服务配置 */
    Map<String, Object> mainServer = new HashMap<>();
}
