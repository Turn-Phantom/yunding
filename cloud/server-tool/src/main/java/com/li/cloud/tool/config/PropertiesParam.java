package com.li.cloud.tool.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc
 * @date 2020-06-11
 */
@Configuration
@ConfigurationProperties(prefix = "server-tool")
@Getter
@Setter
public class PropertiesParam {

    /** 空号检测账号配置 */
    Map<String, String> emptyNumber = new HashMap<>();
}
