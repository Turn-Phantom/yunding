package com.li.cloud.gateway.utils;

/**
 * @desc 常量
 * @date 2020-04-26
 */
public class ConstantUtil {

    /** redis中保存短信验证码的剩余量key */
    public static final String SMS_SURPLUS_NUM = "smsSurplusNumber";

    /** 发送短信验证码模板 */
    public static final String SMS_SEND_MSG = "【云顶】您的%s验证码为：%s，请不要把验证码泄露给其他人！5分钟内有效";

}
