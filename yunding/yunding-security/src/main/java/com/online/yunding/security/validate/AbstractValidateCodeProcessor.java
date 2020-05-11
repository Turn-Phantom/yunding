package com.online.yunding.security.validate;

import com.online.yunding.security.exception.ValidateCodeException;
import com.online.yunding.security.validate.domain.ValidateCode;
import com.online.yunding.security.validate.domain.ValidateCodeType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @desc 验证码处理器，抽象类
 * @date 2020-03-22
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    // 验证码生成器
    @Autowired
    private ValidateCodeGenerator validateCodeGenerator;

    // 验证码持久化操作
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /** 创建验证码并保存 */
    @Override
    public void create(ServletWebRequest webRequest) throws Exception {
        // 生成验证码
        C validateCode = generate(webRequest);

        save(webRequest, validateCode);

        send(webRequest, validateCode);
    }

    /** 调用生成器生成方法 */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request) {
        // 使用验证码生成器调用对应类型的验证码生成器生成验证码
        return (C)validateCodeGenerator.generate(request);
    }

    /** 调用持久化操作，存储验证码 */
    private void save(ServletWebRequest request, C validateCode) {
        // 初始化验证码信息
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        validateCodeRepository.save(request, code, ValidateCodeType.SMS);
    }

    /** 发送验证码 */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /** 校验验证码 */
    @Override
    @SuppressWarnings("unchecked")
    public void validate(ServletWebRequest webRequest) {
        // 通过验证码处理类，获取验证码
        C codeObjInSession = (C) validateCodeRepository.getCode(webRequest, ValidateCodeType.SMS);
        // 从请求中获取登录的手机验证码
        String codeInRequest = "";
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(webRequest.getRequest(), "smsCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }
        if(StringUtils.isBlank(codeInRequest)){
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeObjInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeObjInSession.isExpire()) {
            validateCodeRepository.remove(webRequest, ValidateCodeType.SMS);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equals(codeObjInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码错误");
        }
        // 验证成功后，将验证码对象从session中移除
        validateCodeRepository.remove(webRequest, ValidateCodeType.SMS);
    }


}
