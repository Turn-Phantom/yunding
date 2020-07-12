package com.li.cloud.gateway.security.validate.mobile;

import com.li.cloud.gateway.security.exception.ValidateCodeException;
import com.li.cloud.gateway.security.validate.AbstractValidateCodeProcessor;
import com.li.cloud.gateway.security.validate.ValidateCodeSender;
import com.li.cloud.gateway.security.validate.domain.ValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 短信验证码处理器
 * @date 2020-03-22
 */
@Component("smsCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ValidateCodeSender validateCodeSender;

    /** 短信验证码发送; smsType: 1 注册； 2 登陆； 3 绑定； 4 重置密码 */
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        // 通过请求参数获取手机号码
        String mobilePhone = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        String smsType = request.getRequest().getParameter("smsType");
        if(StringUtils.isBlank(smsType)){
            logger.error("发送验证码失败，验证码参数类型值为空");
            throw new ValidateCodeException("smsType is null");
        }
        // 通过短信验证码发送器接口实现调用发送方法，发送验证码到手机
        validateCodeSender.send(mobilePhone, validateCode.getCode(), Integer.valueOf(smsType), request);
    }
}
