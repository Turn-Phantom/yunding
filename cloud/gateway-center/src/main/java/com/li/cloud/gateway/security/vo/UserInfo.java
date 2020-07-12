package com.li.cloud.gateway.security.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc
 * @date 2020-06-19
 */
@Table(name = "tb_user_info")
@Setter
@Getter
public class UserInfo {

    @Id
    @Column(name = "id")
    private Integer id;

    // 手机号码
    @Column(name = "phoneNo")
    private String phoneNo;

    // 账号
    @Column(name = "accountNo")
    private String accountNo;

    // (会员)登录密码
    @Column(name = "pwd")
    private String pwd;

    // 账号状态; 0 正常，1 锁定；
    @Column(name = "accountStatus")
    private Byte accountStatus;
}
