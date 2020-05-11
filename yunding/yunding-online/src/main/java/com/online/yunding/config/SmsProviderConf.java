package com.online.yunding.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 短信服务商配置
 * @date 2020-04-26
 */
@Configuration
@ConfigurationProperties(prefix = "yunding.params.smsService")
@Getter
@Setter
public class SmsProviderConf {

    // 卡洛思短信服务提供商
    private SmsProvider karlos = new SmsProvider();

}
