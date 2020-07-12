package com.li.cloud.gateway.security.configuration;

import com.li.cloud.gateway.security.session.CusExpiredSessionStrategy;
import com.li.cloud.gateway.security.session.CusInvalidSessionStrategy;
import com.li.cloud.gateway.security.validate.ValidateCodeSender;
import com.li.cloud.gateway.security.validate.mobile.DefaultSmsSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @desc 初始化验证码相关bean
 * @date 2020-03-22
 */
@Configuration
public class InitBeanConfiguration {

    /** 加密bean */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /** 初始化验证码发送器 */
    @Bean
    @ConditionalOnMissingBean(ValidateCodeSender.class)
    public ValidateCodeSender validateCodeSender(){
        return new DefaultSmsSender();
    }

    /** 初始化session失效处理策略 */
    @Bean
    @ConditionalOnMissingBean(InvalidSessionStrategy.class)
    public InvalidSessionStrategy invalidSessionStrategy(){
        return new CusInvalidSessionStrategy();
    }

    /** 初始化并发导致session失效处理策略 */
    @Bean
    @ConditionalOnMissingBean(SessionInformationExpiredStrategy.class)
    public SessionInformationExpiredStrategy sessionInformationExpiredStrategy(){
        return new CusExpiredSessionStrategy();
    }
}
