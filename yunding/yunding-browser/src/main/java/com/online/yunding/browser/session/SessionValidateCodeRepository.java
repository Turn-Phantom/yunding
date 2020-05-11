package com.online.yunding.browser.session;

import com.online.yunding.security.validate.ValidateCodeRepository;
import com.online.yunding.security.validate.domain.ValidateCode;
import com.online.yunding.security.validate.domain.ValidateCodeType;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc: 基于session方式存储的验证码处理器
 * @author: lxs
 * @date: 2020-02-07
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

    /** 操作session工具类 */
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /** 保存sessionKey */
    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        sessionStrategy.setAttribute(request, getSessionKey( validateCodeType), validateCode);
    }

    /** 拼接sessionKey */
    private String getSessionKey(ValidateCodeType validateCodeType){
        /* session中验证码的key值前缀 */
        String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";
        return SESSION_KEY_PREFIX + validateCodeType.toString().toUpperCase();
    }

    /** 从请求获取验证码 */
    @Override
    public ValidateCode getCode(ServletWebRequest request, ValidateCodeType validateCodeType) {
        return (ValidateCode)sessionStrategy.getAttribute(request, getSessionKey(validateCodeType));
    }

    /** 移除session中的验证码 */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        sessionStrategy.removeAttribute(request, getSessionKey(validateCodeType));
    }
}
