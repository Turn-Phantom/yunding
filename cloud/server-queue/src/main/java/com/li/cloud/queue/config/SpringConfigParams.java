package com.li.cloud.queue.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @desc spring配置参数
 * @date 2020-04-09
 */
@Configuration
@ConfigurationProperties(prefix = "spring")
@Getter
@Setter
public class SpringConfigParams {

    // redis配置
    private Map<String, String> redis;
}
