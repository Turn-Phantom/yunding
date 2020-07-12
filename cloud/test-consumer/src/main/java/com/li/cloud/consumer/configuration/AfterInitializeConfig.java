package com.li.cloud.consumer.configuration;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @desc 初始化完成配置后执行
 * @date 2020-05-18
 */
@Component
public class AfterInitializeConfig implements InitializingBean {

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}

