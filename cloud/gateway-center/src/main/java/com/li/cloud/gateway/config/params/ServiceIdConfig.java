package com.li.cloud.gateway.config.params;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 网关服务请求id配置
 * @date 2020-06-22
 */
@Configuration
@ConfigurationProperties(prefix = "cloud.instance.server.request.url-pre")
@Getter
@Setter
public class ServiceIdConfig {

    /** 第三方服务，短信服务前缀 */
    private String sms;
}
