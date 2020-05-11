package com.online.yunding.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @desc
 * @date 2020-04-26
 */
@Getter
@Setter
public class SmsProvider{

    // 用户id
    private String userId;

    // 账号
    private String account;

    // 密码
    private String password;

    // 短信发送地址
    private String sendUrl;

    // 查询剩余量地址
    private String querySurplus;
}