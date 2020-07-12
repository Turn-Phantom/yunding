package com.li.cloud.gateway.config.params;

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
public class SpringConfig {

    // redis配置
    private Map<String, String> redis;
}
