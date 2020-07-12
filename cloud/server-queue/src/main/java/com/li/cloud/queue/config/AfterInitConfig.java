package com.li.cloud.queue.config;

import com.li.cloud.common.entity.RedisConfig;
import com.li.cloud.common.utils.InterAddressUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 初始化完成配置后执行
 * @date 2020-04-09
 */
@Configuration
public class AfterInitConfig implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisConfig redisConfig;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查redis服务
        checkRedisServer();

    }

    /** 检查redis服务 */
    private void checkRedisServer(){
        Map<String, String> redisConf = new HashMap<>();
        redisConf.put("host", redisConfig.getHost());
        redisConf.put("port", redisConfig.getPort()+"");
        boolean isEnable = InterAddressUtils.isEnablePort(redisConf, "redis服务");
        if(!isEnable){
            System.exit(1);
        }
        logger.info("《================= redis 服务正常启动 =================》");
    }

    /** 返回一个RestTemplate实例，默认6秒超时； 默认实例 */
    @Bean(name = "restTemplate")
    public RestTemplate defaultRestTemplate(){
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(6000);
        requestFactory.setReadTimeout(6000);
        return new RestTemplate(requestFactory);
    }

    /** 自定义RestTemplate ； 40秒超时*/
    @Bean(name = "restTemplateForBalance")
    @LoadBalanced
    public RestTemplate cusRestTemplate() {
        return new RestTemplate();
    }

}
