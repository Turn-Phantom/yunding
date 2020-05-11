package com.yunding.server.common.config;

import org.springframework.context.annotation.Configuration;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** 自定义过滤器
 * @desc
 * @date 2020-04-06
 */
@Configuration
public class CusFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

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
        // 放行
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
