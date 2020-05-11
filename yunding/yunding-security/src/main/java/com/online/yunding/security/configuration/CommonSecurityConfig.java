package com.online.yunding.security.configuration;

import com.online.yunding.security.constant.FinalUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @desc 公共安全配置
 * @date 2020-03-22
 */
public class CommonSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler successHandler;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    /** 安全配置 */
    protected void applyPasswordAuthenticationConfig(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(FinalUrl.LOGIN_PROCESSOR)
                .loginProcessingUrl(FinalUrl.LOGIN_FORM)
                .successHandler(successHandler)
                .failureHandler(failureHandler)
        ;
    }
}
