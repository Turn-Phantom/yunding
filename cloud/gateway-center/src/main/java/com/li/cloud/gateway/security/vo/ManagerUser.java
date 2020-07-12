package com.li.cloud.gateway.security.vo;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * @desc
 * @date 2020-06-19
 */
@Table(name = "tb_user_manage")
@Setter
@Getter
public class ManagerUser {

    // 登录账户
    @Column(name = "account")
    private String account;

    // 登录密码
    @Column(name = "password")
    private String password;
}
