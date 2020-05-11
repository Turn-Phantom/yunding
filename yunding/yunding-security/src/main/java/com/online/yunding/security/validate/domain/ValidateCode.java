package com.online.yunding.security.validate.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @desc 验证码实体
 * @date 2020-03-21
 */
@Getter
@Setter
public class ValidateCode implements Serializable {

    private static final long serialVersionUID = 5803109473580214877L;

    private String code;
    private LocalDateTime expireTime;

    /** 初始化验证码信息 */
    public ValidateCode(String code, int expireSecond){
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSecond);
    }

    public ValidateCode(String code, LocalDateTime localDateTime){
        this.code = code;
        this.expireTime = localDateTime;
    }

    /** 判断验证码是否过期 */
    public boolean isExpire(){
        return LocalDateTime.now().isAfter(expireTime);
    }
}
