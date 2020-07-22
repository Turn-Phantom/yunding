package com.li.cloud.gateway.security.authentication;

import com.li.cloud.gateway.security.prop.OAth2ClientProp;
import com.li.cloud.gateway.security.prop.SecurityConfigProp;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;


/**
 * @desc 自定义认证服务
 * @date 2020-03-22
 */
@Configuration
@EnableAuthorizationServer
public class CusAuthorizationServer extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private SecurityConfigProp securityConfigProp;

    /** 注入认证管理器; springboot2.X 不能直接注入，通过初始化bean解决：{@link WebSecurityConfig} */
    @Autowired
    private AuthenticationManager authenticationManager;

    // 注入用户认证服务
    @Autowired
    private UserDetailsService userDetailsService;

    // 注入令牌存储配置
    @Autowired
    private TokenStore tokenStore;

    // 注入jwt token转换器，默认不自动初始化加载
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    // 注入jwt增强器
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /** 认证管理配置 */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore) // 通过自定义token配置存储，不使用默认的内存存储
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
        ;
        // 增加jwt生成token判断,当不为空时，使用自定义的token生成方式
        if(jwtAccessTokenConverter != null && jwtTokenEnhancer != null){
            // 新建增强器，通过增强器处理
            TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> chainList = new ArrayList<>();
            chainList.add(jwtTokenEnhancer);
            chainList.add(jwtAccessTokenConverter);
            tokenEnhancerChain.setTokenEnhancers(chainList);

            // 设置增强器链
            endpoints.tokenEnhancer(tokenEnhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }
    }

    /** 客户端相关配置；替代原有默认的clientId和clientSecret */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        InMemoryClientDetailsServiceBuilder inMemoryBuild = clients.inMemory();
        // 判断是否配置客户端信息
        if(ArrayUtils.isEmpty(securityConfigProp.getOauth2().getClients())){
            return;
        }

        for (OAth2ClientProp config : securityConfigProp.getOauth2().getClients()) {
            inMemoryBuild
                    .withClient(config.getClientId()) // clientId
                    .secret(passwordEncoder.encode(config.getClientSecret())) // springboot2.x版本的坑，此处需要使用passwordEncoder进行加密
                    .authorizedGrantTypes("authorization_code", "refresh_token","password") // 指定授权类型，默认支持有所，若指定，则只允许指定的授权类型生效
                    .accessTokenValiditySeconds(config.getTokenInvalidTime()) // 令牌的有效时间， 单位/秒
                    .refreshTokenValiditySeconds(config.getRefreshTokenInvalidTime()) // 刷新令牌有效时间， 单位：秒
                    .scopes("all"); // 指定授权的权限有哪些
        }
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        super.configure(security);
    }
}
