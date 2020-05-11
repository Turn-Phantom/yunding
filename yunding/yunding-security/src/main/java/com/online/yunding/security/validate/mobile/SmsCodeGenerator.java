package com.online.yunding.security.validate.mobile;

import com.online.yunding.security.prop.SecurityConfigProp;
import com.online.yunding.security.validate.ValidateCodeGenerator;
import com.online.yunding.security.validate.domain.ValidateCode;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 短信验证码生成器
 * @date 2020-03-21
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

    @Autowired
    private SecurityConfigProp securityConfigProp;

    /** 验证码生成 */
    @Override
    public ValidateCode generate(ServletWebRequest webRequest) {
        String code = RandomStringUtils.randomNumeric(securityConfigProp.getValidateCode().getCodeLen());
        return new ValidateCode(code, securityConfigProp.getValidateCode().getExpireTime());
    }
}
