package com.li.cloud.gateway.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.li.cloud.gateway.security.prop.SecurityConfigProp;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc 自定义认证成功处理器
 * @date 2020-03-21
 */
@Component
public class CusAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityConfigProp securityConfigProp;

    // 注入ClientDetailsService，获取ClientDetails
    @Autowired
    private ClientDetailsService clientDetailsService;

    // 注入AuthorizationServerToken服务
    @Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 认证成功处理 */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功！");
        String responseHeader = response.getHeader("Access-Control-Allow-Origin");
        if(StringUtils.isEmpty(responseHeader) || !"*".equals(responseHeader)){
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Headers", "*");
            response.setHeader("Access-Control-Allow-Methods", "*");
        }
        // Authorization ； 封装在请求参数中，由clientid和clientSecret
        String header = request.getHeader("Authorization");
        if(header == null || !header.startsWith("Basic ")){
            throw new UnapprovedClientAuthenticationException("请求头中无client信息");
        }
        // 获取解码后的信息
        String[] tokens = extractAndDecodeHeader(header,request);
        assert tokens.length == 2;

        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 获取ClientDetails
        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);
        if(clientDetails == null){
            throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在：" + clientId);
        } else if (!passwordEncoder.matches(tokens[1], clientDetails.getClientSecret())){
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配：" + clientSecret);
        }

        // 获取TokenRequest
        /* 参数：
            1、4种授权模式中存在各自特定的参数，根据不同授权模式，传不同的参数，此处自定义，在该处理器已存在authentication信息
            2、clientId
            3、应用的权限，由clientDetails中配置，直接获取
            4、授权模式，此处自定义
          */
        TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

        // 创建OAuth2Request
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

        // 构建OAuth2Authentication
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);

        // 生成令牌
        OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);

        response.setContentType("application/json; charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(token));
    }

    /**
     * @desc: 抽取并解码请求头中封装的Authorization信息
     * @param: header 请求头client信息
     * @param: request http请求
     * @return: 用户名和面
     * @date: 2020-02-06
     */
    private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException{
        byte[] base64Token = header.substring(6).getBytes("UTF-8");
        byte[] decode;
        // 对请求头中的Authorization信息进行base64解码
        try {
            decode = Base64.decodeBase64(base64Token);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("Authorization参数解码失败");
        }
        String token = new String(decode, "utf-8");
        int delim = token.indexOf(":");
        if(delim == -1){
            throw new BadCredentialsException("无效的Authorization信息");
        }
        // 返回用户名和密码
        return new String[] {token.substring(0, delim), token.substring(delim + 1)};
    }
}
