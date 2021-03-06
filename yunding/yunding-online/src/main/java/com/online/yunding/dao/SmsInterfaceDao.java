package com.online.yunding.dao;

import com.online.yunding.entity.SmsSendRecord;

import java.util.Map;

/**
 * @desc
 * @date 2020-04-26
 */
public interface SmsInterfaceDao {

    /** 保存短信发送记录 */
    int saveSendRecord(SmsSendRecord smsSendRecord);

    /** 查询临时管理员手机信息 */
    Map<String, String> queryManagerContact();
}
