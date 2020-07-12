package com.li.cloud.online.config;

import com.li.cloud.common.utils.InterAddressUtils;
import com.li.cloud.online.config.params.SpringConfigParams;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @desc 参数初始化完成后检查
 * @date 2020-04-08
 */
@Component
public class AfterInitializingCheck implements InitializingBean {

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
        boolean isEnable = InterAddressUtils.isEnablePort(redisConf, "redis服务");
        if(!isEnable){
            System.exit(1);
        }
    }
}
