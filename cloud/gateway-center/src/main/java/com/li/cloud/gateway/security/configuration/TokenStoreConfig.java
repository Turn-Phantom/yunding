package com.li.cloud.gateway.security.configuration;
import com.li.cloud.gateway.security.jwt.CusJwtTokenEnhancer;
import com.li.cloud.gateway.security.prop.SecurityConfigProp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @desc: 令牌存储配置
 *      通过配置文件，设置两种token生成模式
 *       ①通过redis存储，默认生成
 *       ②通过jwt生成
 * @author: lxs
 * @date: 2020-02-08
 */
@Configuration
public class TokenStoreConfig {

    // 注入redis链接工厂
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    // 令牌存储配置
    @Bean
    // 检查配置文件中的前缀(prefix)：yunding.online.oauth2， 属性为(name):storeType的值，是否有值且值为(havingValue)：redis
    @ConditionalOnProperty(prefix = "yunding.online.oauth2", name = "storeType", havingValue = "redis")
    public TokenStore redisTokenStore(){
        return new RedisTokenStore(redisConnectionFactory);
    }

    /** JWT自定义生成令牌配置
     *      通过自定义生成令牌，可以替换默认生成的令牌
     * */
    @Configuration
    // 检查配置文件中的前缀(prefix)：yunding.online.oauth2， 属性为(name):storeType的值，是否有值且值为(havingValue)：jwt， 匹配上，则该配置生效（matchIfMissing）
    @ConditionalOnProperty(prefix = "yunding.online.oauth2", name = "storeType", havingValue = "jwt", matchIfMissing = true)
    public static class JwtTokenConfig{

        // 注入安全配置属性读取器
        @Autowired
        private SecurityConfigProp securityConfigProp;

        /** 声明token存储处理bean  */
        @Bean
        public TokenStore jtwTokenStore(){
            return new JwtTokenStore(jwtAccessTokenConverter());
        }

        /** 声明token生成处理bean,使用内置秘钥签名 */
        @Bean
        public JwtAccessTokenConverter jwtAccessTokenConverter(){
            // 通过该类对token进行密签等，加密操作
            JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
            jwtAccessTokenConverter.setSigningKey(securityConfigProp.getOauth2().getJwtSigningKey()); // 设置签名的秘钥
            return jwtAccessTokenConverter;
        }

        /**  声明jwt token自定义增强处理bean */
        @Bean
        // 指定bean名称，其他类指定bean名称，覆盖当前实现逻辑，当前bean为默认实现
        @ConditionalOnMissingBean(name = "jwtTokenEnhancer")
        public TokenEnhancer jwtTokenEnhancer(){
            return new CusJwtTokenEnhancer();
        }
    }
}
