package com.online.yunding.security.validate;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 验证码处理器接口
 * @date 2020-03-22
 */
public interface ValidateCodeProcessor {

    /**
     * @desc 生成
     * @param webRequest
     * @return
     * @date 2020-03-22
     */
    void create(ServletWebRequest webRequest) throws Exception;

    /**
     * @desc 校验
     * @param webRequest
     * @return void
     * @date 2020-03-22
     */
    void validate(ServletWebRequest webRequest);
}
