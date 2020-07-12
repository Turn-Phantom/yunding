package com.li.cloud.queue.config.params;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 请求服务实例id
 * @date 2020-06-22
 */
@Configuration
@ConfigurationProperties(prefix = "cloud.instance.server.request.url-pre")
@Getter
@Setter
public class ServiceIdConfig {

    // 云顶在线服务
    private String onlineUrl;

    // 第三方接口管理服务
    private String thirdUrl;
}
