package com.online.yunding.security.prop;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc: oath2相关配置类
 * @date: 2020-02-08
 */
@Getter
@Setter
public class OAuth2Prop {

    // jwt自定生成令牌的签名key
    private String jwtSigningKey = "yunding-online-key";

    // 第三方认证授权客户端配置
    private OAth2ClientProp[] clients = {};
}
