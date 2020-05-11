package com.yunding.server.queue.config;

import com.yunding.server.common.utils.InterAddressUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @desc 初始化完成配置后执行
 * @date 2020-04-09
 */
@Configuration
public class AfterInitConfig implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SpringConfigParams springConfigParams;

    @Override
    public void afterPropertiesSet() throws Exception {
        // 检查redis服务
        checkRedisServer();

    }

    /** 检查redis服务 */
    private void checkRedisServer(){
        Map<String, String> redisConf = springConfigParams.getRedis();
        if(null == redisConf){
            logger.error("启动失败：未配置redis服务地址信息");
            System.exit(1);
        }
        String host = redisConf.get("host");
        String port = redisConf.get("port");
        if(StringUtils.isEmpty(host)){
            logger.error("启动失败：未配置redis服务主机地址");
            System.exit(1);
        }
        if(StringUtils.isEmpty(port)){
            logger.error("启动失败：未配置redis服务主机端口");
            System.exit(1);
        }
        boolean enablePort = InterAddressUtils.isEnablePort(host, Integer.valueOf(port));
        if(!enablePort){
            logger.error("启动失败：无法访问redis服务");
            System.exit(1);
        }
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
    @Bean(name = "cusRestTemplate")
    public RestTemplate cusRestTemplate() {
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().build());
        httpRequestFactory.setConnectionRequestTimeout(40000);
        httpRequestFactory.setConnectTimeout(40000);
        httpRequestFactory.setReadTimeout(40000);
        return new RestTemplate(httpRequestFactory);
    }

}
