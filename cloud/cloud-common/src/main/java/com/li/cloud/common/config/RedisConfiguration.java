package com.li.cloud.common.config;

import com.li.cloud.common.entity.RedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;

/**
 * @desc 自定义redis配置
 * @date 2020-07-02
 */
//@Configuration
public class RedisConfiguration extends RedisStandaloneConfiguration {

    @Autowired
    private RedisConfig redisConfig;

    /** 默认构造设置主机和端口 */
    public RedisConfiguration(){
        this.setHostName(redisConfig.getHost());
        this.setPort(redisConfig.getPort());
    }
}
