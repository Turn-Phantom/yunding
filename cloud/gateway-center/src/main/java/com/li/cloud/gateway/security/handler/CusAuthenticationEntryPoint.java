package com.li.cloud.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.cloud.common.basecurd.entity.ReturnData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc
 * @date 2020-04-05
 */
@Component
public class CusAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(200);
        response.setContentType("application/json;charset=utf-8");
        String header = response.getHeader("Access-Control-Allow-Origin");
        if(StringUtils.isEmpty(header) || !"*".equals(header)){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
        }
        response.getWriter().write(objectMapper.writeValueAsString(ReturnData.error(authException.getMessage())));
    }
}
