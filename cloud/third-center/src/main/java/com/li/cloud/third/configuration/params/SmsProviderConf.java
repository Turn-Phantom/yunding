package com.li.cloud.third.configuration.params;

import com.li.cloud.third.configuration.pojo.SmsProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 短信服务商配置
 * @date 2020-04-26
 */
@Configuration
@ConfigurationProperties(prefix = "third-center.params.sms-service")
@Getter
@Setter
public class SmsProviderConf {

    // 卡洛思短信服务提供商
    private SmsProvider karlos = new SmsProvider();

}
