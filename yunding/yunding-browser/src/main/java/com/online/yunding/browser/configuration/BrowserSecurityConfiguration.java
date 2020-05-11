package com.online.yunding.browser.configuration;
import com.online.yunding.security.configuration.CommonSecurityConfig;
import com.online.yunding.security.configuration.ValidateCodeSecurityConfig;
import com.online.yunding.security.constant.FinalUrl;
import com.online.yunding.security.mobile.SmsAuthenticationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * @desc 浏览器安全配置
 * @date 2020-03-22
 */
@Configuration
public class BrowserSecurityConfiguration extends CommonSecurityConfig {
    // 验证码安全配置（增加验证码过滤器）
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    // 短信认证配置（短信登录自定义认证）
    @Autowired
    private SmsAuthenticationConfiguration smsConfiguration;

    // 登录session并发导致失效处理策略
    @Autowired
    private SessionInformationExpiredStrategy expiredSessionStrategy;

    // 登录session超时失效处理策略
    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    // 退出成功处理器
    @Autowired
    private LogoutSuccessHandler logoutHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 应用公共配置
        applyPasswordAuthenticationConfig(http);

        http.apply(validateCodeSecurityConfig) // 加入验证码配置
                .and()
                .apply(smsConfiguration) // 加入短信认证授权配置
                .and()
                // session管理
                .sessionManagement()
                .invalidSessionStrategy(invalidSessionStrategy) // session失效处理策略
                .maximumSessions(2) // 每个用户最多允许同时在线两台设备
                .expiredSessionStrategy(expiredSessionStrategy) // 相同用户session超过最大值后，使用session策略处理
                .and()
                .and()
                // 退出登录
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(logoutHandler) // 退出成功处理
                .deleteCookies("JSESSIONID")
                .and()
                // 认证授权
                .authorizeRequests()
                .antMatchers(FinalUrl.LOGIN_PROCESSOR,// 登录处理器地址
                        FinalUrl.LOGIN_FORM, // 登录表单
                        FinalUrl.SMS_CODE_SEND, // 验证码发送
                        FinalUrl.LOGIN_SMS, // 验证码登录
                        FinalUrl.RECORD_IP // 记录ip信息
                ) .permitAll() // 放行
                .anyRequest() // 任何请求
                .authenticated() // 需要身份认证
                .and()
                .csrf().disable();
    }
}
