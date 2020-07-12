package com.li.cloud.video.config;

import com.li.cloud.video.entity.DbSystemParams;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc spring配置参数
 * @date 2020-04-09
 */
@Configuration
@ConfigurationProperties(prefix = "yunding.video")
@Getter
@Setter
public class YunDingParams {

    // 数据库系统参数
    Map<String, DbSystemParams> dbSystemParams = new HashMap<>();

    // 影片相关配置
    VideoConfig videoConfig = new VideoConfig();


}
