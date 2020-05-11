package com.online.yunding.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 自定义配置参数
 * @date 2020-04-08
 */
@Configuration
@ConfigurationProperties(prefix = "yunding.params")
@Getter
@Setter
public class ConfigParams {
    // 队列服务地址
    private ServerObj queueServer = new ServerObj();

    // 视频服务
    private ServerObj videoServer = new ServerObj();

    // 推送服务
    private Map<String, String> webSocketServer = new HashMap<>();
}
