package com.li.cloud.gateway.security.prop;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc 验证码相关配置
 * @date 2020-03-21
 */
@Getter
@Setter
public class ValidateCodeProp {
    private byte codeLen = 6;

    // 单位：秒
    private int expireTime = 60;

}
