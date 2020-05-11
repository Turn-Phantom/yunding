package com.online.yunding.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @desc 自定义验证码异常处理
 * @date 2020-03-22
 */
public class ValidateCodeException extends AuthenticationException {

    public ValidateCodeException(String msg, Throwable t){
        super(msg, t);
    }

    public ValidateCodeException(String msg){
        super(msg);
    }
}
