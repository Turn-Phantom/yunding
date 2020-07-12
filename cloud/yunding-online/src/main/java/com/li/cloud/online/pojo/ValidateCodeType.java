package com.li.cloud.online.pojo;

/**
 * @desc: 验证码类型
 * @date: 2019-11-21
 */
public enum ValidateCodeType {

    /** 短信验证码 */
    SMS{
        @Override
        public String getParamNameOnValidate(){
            return "SMS";
        }
    };

    /**
     * 校验时从请求中获取的参数的名字
     * @return
     */
    public abstract String getParamNameOnValidate();
}
