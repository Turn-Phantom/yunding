package com.li.cloud.third.service;

import com.li.cloud.third.entity.SmsSendRecord;

import java.util.Map;

/**
 * @desc 短信接口服务 接口
 * @date 2020-04-26
 */
public interface SmsInterfaceService{

    /** redis中保存短信验证码的剩余量key */
    String SMS_SURPLUS_NUM = "smsSurplusNumber";

    /** 查询短信剩余量 */
    Map<String, String> querySmsNum();

    /** 发送短信 */
    Map<String, String> sendSmsCode(String phone, String content);

    /** 保存短信发送记录 */
    int saveSendRecord(SmsSendRecord smsSendRecord);
}
