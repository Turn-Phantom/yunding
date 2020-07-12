package com.li.cloud.gateway.security.mobile.configuration;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @desc 短信验证码认证token
 * @date 2020-03-22
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    private static final long serialVersionUID = 420L;

    @Getter
    private final Object principal;

    /** 未登录情况 */
    public SmsAuthenticationToken(String mobile){
        super(null);
        this.principal = mobile;
        this.setAuthenticated(false);
    }

    /** 登录成功处理 */
    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities){
        super(authorities);
        this.principal = principal;
        this.setAuthenticated(true);
    }

    public Object getCredentials() {
        return null;
    }

    public void eraseCredentials() {
        super.eraseCredentials();
    }
}
