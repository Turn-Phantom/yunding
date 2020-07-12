package com.li.cloud.common.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc
 * @date 2020-07-02
 */
@Configuration
@ConfigurationProperties(prefix = "spring.redis")
@Getter
@Setter
public class RedisConfig {

    private String host;

    private Integer port;
}
