package com.li.cloud.gateway.security.validate;

import com.li.cloud.gateway.security.validate.domain.ValidateCode;
import com.li.cloud.gateway.security.validate.domain.ValidateCodeType;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc: 验证码存储器接口
 * @date: 2020-02-07
 * @author: lxs
 */
public interface ValidateCodeRepository {

    /** 保存验证码 */
    void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

    /** 获取验证码 */
    ValidateCode getCode(ServletWebRequest request, ValidateCodeType validateCodeType);

    /** 移除验证码 */
    void remove(ServletWebRequest request, ValidateCodeType validateCodeType);

}
