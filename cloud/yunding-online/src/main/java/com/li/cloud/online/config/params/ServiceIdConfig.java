package com.li.cloud.online.config.params;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @desc 云顶在线服务 请求服务id
 * @date 2020-06-22
 */
@Configuration
@ConfigurationProperties(prefix = "cloud.instance.server.request.url-pre")
@Getter
@Setter
public class ServiceIdConfig {

    // 视频服务
    private String videoUrl;

    // 队列服务
    private String queueUrl;

    //网关服务
    private String gatewayUrl;
}
