package com.online.yunding.service;

import com.online.yunding.entity.SmsSendRecord;

import java.util.Map;

/**
 * @desc 短信接口服务 接口
 * @date 2020-04-26
 */
public interface SmsInterfaceService{

    /** 查询短信剩余量 */
    Map<String, String> querySmsNum();

    /** 发送短信 */
    Map<String, String> sendSmsCode(String phone, String content);

    /** 保存短信发送记录 */
    int saveSendRecord(SmsSendRecord smsSendRecord);
}
