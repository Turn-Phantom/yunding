package com.li.cloud.gateway.security.validate.mobile;

import com.li.cloud.gateway.security.validate.ValidateCodeSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 默认短信发送器
 * @date 2020-03-22
 */
public class DefaultSmsSender implements ValidateCodeSender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 发送短信验证码 */
    @Override
    public void send(String mobile, String code, Integer smsType, ServletWebRequest request) {
        logger.info("默认短信验证码发送器执行");
//        logger.info(String.format("开始发送短信验证码，手机号码为：%s,验证码为：%s;", mobile, code));
//        logger.info("未接入短信服务商，仅提供测试");
    }
}
