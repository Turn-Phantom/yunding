package com.li.cloud.gateway.security.jwt;

import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Hashtable;
import java.util.Map;

/**
 * @desc: 自定义jwt令牌增强器
 * @author: lxs
 * @date: 2020-02-08
 */
public class CusJwtTokenEnhancer implements TokenEnhancer {

    /** 令牌处理方法 */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        // 增强信息
        Map<String, Object> info = new Hashtable<>();
//        info.put("company", System.currentTimeMillis());
        // 强转，设置增强信息
        ((DefaultOAuth2AccessToken)accessToken).setAdditionalInformation(info);
        return accessToken;
    }
}
