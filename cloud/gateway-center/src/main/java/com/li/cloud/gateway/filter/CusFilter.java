package com.li.cloud.gateway.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 自定义过滤器
 * @date 2020-06-02
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // 作用，在springSecurity前执行，放行/oauth/token的options请求
public class CusFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //指定允许其他域名访问
        response.setHeader("Access-Control-Allow-Origin", "*");
        //响应头设置
        response.setHeader("Access-Control-Allow-Headers", "*");
        //响应类型
        response.setHeader("Access-Control-Allow-Methods", "*");
        String method = request.getMethod();

        if("OPTIONS".equalsIgnoreCase(method)){
        }
        /** 当请求为options，且为刷新token的预检时，提前返回，否则会经过springSecurity处理，无法通过预检，导致无法刷新token */
        else if("OPTIONS".equalsIgnoreCase(method) && request.getRequestURI().equals("/oauth/token")) {
            response.getWriter().write("");
            response.flushBuffer();
        } else{
            filterChain.doFilter(request, response);
        }
    }
}
