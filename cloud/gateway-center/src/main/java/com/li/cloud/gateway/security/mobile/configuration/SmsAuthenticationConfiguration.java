package com.li.cloud.gateway.security.mobile.configuration;

import com.li.cloud.gateway.security.mobile.filter.SmsAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @desc 短信认证授权配置
 * @date 2020-03-22
 */
@Component
public class SmsAuthenticationConfiguration extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public void configure(HttpSecurity security) throws Exception {
        // 设置过滤器的授权管理者
        SmsAuthenticationFilter smsFilter = new SmsAuthenticationFilter();
        smsFilter.setAuthenticationManager(security.getSharedObject(AuthenticationManager.class));
        // 设置成功/失败处理器
        smsFilter.setAuthenticationSuccessHandler(successHandler);
        smsFilter.setAuthenticationFailureHandler(failureHandler);

        // 配置认证授权提供者
        SmsAuthenticationProvider smsProvider = new SmsAuthenticationProvider();
        smsProvider.setUserDetailsService(userDetailsService);

        security.authenticationProvider(smsProvider) // 加入自定义提供者
                .addFilterAfter(smsFilter, UsernamePasswordAuthenticationFilter.class); // 短信认证过滤器加入用户名密码过滤器后面

    }
}
