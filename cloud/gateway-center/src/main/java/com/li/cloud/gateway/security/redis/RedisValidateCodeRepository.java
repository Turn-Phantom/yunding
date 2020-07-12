package com.li.cloud.gateway.security.redis;

import com.li.cloud.gateway.security.exception.ValidateCodeException;
import com.li.cloud.gateway.security.validate.ValidateCodeRepository;
import com.li.cloud.gateway.security.validate.domain.ValidateCode;
import com.li.cloud.gateway.security.validate.domain.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * @desc: 基于第三方请求的验证码处理（可用与分布式应用，资源服务请求等）
 *      使用redis存储验证码，并由redis决定验证码的有效期
 * @author: lxs
 * @date: 2020-02-07
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

    // 注入RedisTemplate工具
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    /** 保存验证码 */
    @Override
    public void save(ServletWebRequest request, ValidateCode validateCode, ValidateCodeType validateCodeType) {
        // 存储验证码到redis服务，保留三十分钟，超过时间后自动清除
        redisTemplate.opsForValue().set(buildKey(request, validateCodeType), validateCode, 30, TimeUnit.MINUTES);
    }

    /** 获取验证码 */
    @Override
    public ValidateCode getCode(ServletWebRequest request, ValidateCodeType validateCodeType) {
        Object code = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
        if(code == null){
            return null;
        }
        return (ValidateCode)code;
    }

    /** 移除验证码 */
    @Override
    public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
        redisTemplate.delete(buildKey(request, validateCodeType));
    }

    /** 构建redis存储验证码的key */
    private Object buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
        String deviceId = request.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
    }
}
