package com.li.cloud.common.basecurd.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

/**
 * @desc:
 * @date: 2019-06-22
 */
@Getter
@PropertySource("classpath:config.properties")
public class ConfigParam {

    @Value("${spring.databaseType}")
    private String databaseType;
}
