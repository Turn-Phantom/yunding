package com.li.cloud.gateway.security.validate;

import com.li.cloud.gateway.security.validate.domain.ValidateCode;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 验证码生成器
 * @date 2020-03-21
 */
public interface ValidateCodeGenerator {

    /**
     * @desc 验证码生成
     * @param webRequest
     * @return ValidateCode
     * @date 2020-03-21
     */
    ValidateCode generate(ServletWebRequest webRequest);
}
