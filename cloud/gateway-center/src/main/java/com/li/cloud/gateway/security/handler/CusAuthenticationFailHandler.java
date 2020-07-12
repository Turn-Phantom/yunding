package com.li.cloud.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.cloud.common.basecurd.entity.ReturnData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 认证失败处理器
 * @date 2020-03-21
 */
@Component
public class CusAuthenticationFailHandler extends SimpleUrlAuthenticationFailureHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    /** 认证失败处理 */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        logger.info("用户认证失败：" + exception.getMessage());
        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        String header = response.getHeader("Access-Control-Allow-Origin");
        if(StringUtils.isEmpty(header) || !"*".equals(header)){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
        }
        response.getWriter().write(objectMapper.writeValueAsString(ReturnData.error(exception.getMessage())));
    }
}
