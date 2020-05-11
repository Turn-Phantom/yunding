package com.online.yunding.security.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @desc 属性配置类
 * @date 2020-03-21
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "yunding.online")
public class SecurityConfigProp {

    // 浏览器相关配置
    BrowserProp browser = new BrowserProp();

    // 验证码相关配置
    ValidateCodeProp validateCode = new ValidateCodeProp();

    // 第三方配置
    OAuth2Prop oauth2 = new OAuth2Prop();
}
