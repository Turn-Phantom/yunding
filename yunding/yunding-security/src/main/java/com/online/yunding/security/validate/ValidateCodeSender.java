package com.online.yunding.security.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 验证码发送器
 * @date 2020-03-21
 */
public interface ValidateCodeSender {
    /**
     * @desc 验证码发送
     * @param mobile 手机号码
     * @param code 验证码
     * @param smsType 验证码类型： 1注册； 2 登陆； 3绑定
     * @return void
     * @date 2020-03-21
     */
    void send(String mobile, String code, Integer smsType, ServletWebRequest request);
}
