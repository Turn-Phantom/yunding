package com.li.cloud.gateway.security.mobile.filter;

import com.li.cloud.gateway.security.mobile.configuration.SmsAuthenticationToken;
import lombok.Setter;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 短信认证过滤器
 * @date 2020-03-22
 */
public class SmsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    @Setter
    private boolean postOnly = true;

    /** 初始化短信认证请求路径 */
    public SmsAuthenticationFilter(){
        super(new AntPathRequestMatcher("/authentication/mobile", "POST"));
    }

    /** 认证逻辑 */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // 请求方法必须为post
        if(this.postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }  else {
            String mobile = this.obtainMobile(request);
            if (mobile == null) {
                mobile = "";
            }
            mobile = mobile.trim();
            SmsAuthenticationToken authRequest = new SmsAuthenticationToken(mobile);
            this.setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    /** 从请求中获取手机号码 */
    protected String obtainMobile(HttpServletRequest request) {
        return request.getParameter("mobile");
    }

    protected void setDetails(HttpServletRequest request, SmsAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
