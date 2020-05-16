package com.online.yunding.app.authentication;

import com.online.yunding.security.configuration.ValidateCodeSecurityConfig;
import com.online.yunding.security.constant.FinalUrl;
import com.online.yunding.security.mobile.SmsAuthenticationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @desc: 自定义资源服务器
 *      继承资源服务配置适配器
 * @date: 2020-02-05
 */
@Configuration
@EnableResourceServer // 该注解表明，该服务为资源服务器（逻辑概念，认证服务器和资源服务器既可同一台机器，也可不同）
public class CustomResourceServer extends ResourceServerConfigurerAdapter {

    // 认证成功处理器
    @Autowired
    private AuthenticationSuccessHandler successHandler;

    // 认证失败处理器
    @Autowired
    private AuthenticationFailureHandler failureHandler;

    // 注入验证码安全配置
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    // 注入自定义的短信验证码认证授权安全配置
    @Autowired
    private SmsAuthenticationConfiguration smsConfig;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    /** 资源安全配置 */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage(FinalUrl.LOGIN_PROCESSOR) // 配置登录页面
                .loginProcessingUrl(FinalUrl.LOGIN_FORM) // 配置登录表单请求url
                .successHandler(successHandler)
                .failureHandler(failureHandler)
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                ;
        http.apply(validateCodeSecurityConfig) // 应用验证码安全配置
                .and()
                .apply(smsConfig) // 短信验证码配置
                .and()
                .authorizeRequests()
                .antMatchers(FinalUrl.LOGIN_PROCESSOR, // 表单登录处理请求
                        FinalUrl.LOGIN_SMS, // 短信登录请求
                        FinalUrl.LOGIN_FORM, // 登录页面请求
                        FinalUrl.SMS_CODE_SEND, // 验证码发送
                        FinalUrl.QUERY_HAS_USER,// 查询用户是否存在
                        FinalUrl.USER_REGISTER, // 用户注册
                        FinalUrl.RECORD_IP, // 记录ip信息
                        FinalUrl.VIDEO_CLASS_LIST, // 获取影片清单分类数据
                        FinalUrl.VIDEO_HOT_VIDEO, // 获取影片热播清单
                        FinalUrl.QUERY_RCMD_VIDEO, // 获取推荐影片清单
                        FinalUrl.QUERY_INFO_BY_VIDEO_ID, // 根据影片id获取影片信息
                        FinalUrl.VIDEO_PLAY_COUNT, // 影片观看次数统计
                        FinalUrl.QUERY_HEIGHT_VIDEO, // 首页播放频率最高，且高清的视频
                        FinalUrl.FREE_VIDEO_TOKEN, // 获取免费电影TOKEN
                        FinalUrl.QUERY_SCROLL_DATA, // 获取首页滚动数据，虚拟
                        FinalUrl.QUERY_SMS_SURPLUS, // 查询短信剩余量
                        FinalUrl.WEB_SOCKET_LINK, // web socket 连接
                        FinalUrl.ADVERT_LINK_DATA, // 获取广告链接数据
                        FinalUrl.RESET_PASSWORD_URL_SMSCODE, // 放行验证重置密码验证码请求
                        FinalUrl.RESET_PASSWORD_URL_PASSWORD // 放行重置密码请求
                )
                .permitAll() // 放行上面的url
                .anyRequest() // 任何请求
                .authenticated() // 需要身份认证
                .and()
                .csrf().disable()
        ;
    }
}
