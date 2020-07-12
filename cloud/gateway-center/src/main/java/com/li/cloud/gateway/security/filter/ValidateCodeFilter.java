package com.li.cloud.gateway.security.filter;

import com.li.cloud.gateway.security.constant.FinalUrl;
import com.li.cloud.gateway.security.exception.ValidateCodeException;
import com.li.cloud.gateway.security.validate.ValidateCodeProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 验证码过滤器
 * @date 2020-03-22
 */
@Component("validateCodeFilter")
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateCodeFilter.class);

    @Autowired
    private ValidateCodeProcessor validateCodeProcessor;

    @Autowired
    private AuthenticationFailureHandler failureHandler;

    /** 过滤器方法 */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 当前请求为短信登录时，执行
        if(request.getRequestURI().contains(FinalUrl.LOGIN_SMS)){
            LOGGER.info("短信验证码过滤器执行！");
            try {
                // 调用验证码处理器校验
                validateCodeProcessor.validate(new ServletWebRequest(request));
            } catch (ValidateCodeException e) {
                // 通过认证授权失败处理器将自定义捕获到的异常抛出
                failureHandler.onAuthenticationFailure(request, response, e);
                return;
            }
        }

        // 过滤器链放行
        filterChain.doFilter(request, response);
    }

    /** 在所有配置参数初始化完成后执行 */
    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
    }
}
