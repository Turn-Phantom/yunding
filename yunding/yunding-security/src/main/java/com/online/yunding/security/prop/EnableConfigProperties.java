package com.online.yunding.security.prop;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 启用配置属性
 * @date 2020-03-21
 */
@Configuration
@EnableConfigurationProperties(SecurityConfigProp.class)
public class EnableConfigProperties {
}
