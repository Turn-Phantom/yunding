package com.online.yunding.security.prop;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc: oath2客户端相关配置
 * @date: 2020-02-08
 */
@Getter
@Setter
public class OAth2ClientProp {

    private String clientId;

    private String clientSecret;

    // 令牌失效时间,单位/秒
    private int tokenInvalidTime;

    // 刷新令牌有效时间；单位：秒
    private int refreshTokenInvalidTime;

}
