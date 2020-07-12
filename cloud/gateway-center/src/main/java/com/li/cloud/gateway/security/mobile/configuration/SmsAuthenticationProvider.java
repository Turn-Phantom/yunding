package com.li.cloud.gateway.security.mobile.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @desc 短信认证提提供器
 * @date 2020-03-22
 */
public class SmsAuthenticationProvider implements AuthenticationProvider {

    @Getter
    @Setter
    private UserDetailsService userDetailsService;

    /** 认证授权逻辑 */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 转换认证信息
        SmsAuthenticationToken smsToken = (SmsAuthenticationToken) authentication;
        // 获取用户信息
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) smsToken.getPrincipal());
        if(userDetails == null){
            throw new InternalAuthenticationServiceException("获取用户信息失败！");
        }
        // 认证授权
        SmsAuthenticationToken authenticationResult = new SmsAuthenticationToken(userDetails, userDetails.getAuthorities());
        authenticationResult.setDetails(smsToken.getDetails());
        return authenticationResult;
    }

    /** 查找认证处理类 */
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
