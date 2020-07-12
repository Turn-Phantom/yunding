package com.li.cloud.online.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 后台管理员用户
 * @date 2020-04-12
 */
@Table(name = "tb_user_manage")
@Alias("managerUser")
@Setter
@Getter
@Entity
@ToString
public class ManagerUser {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 登录账户
    @Column(name = "account")
    private String account;

    // 登录密码
    @Column(name = "password")
    private String password;

    // 登录状态； 0正常； 1锁定
    @Column(name = "accountStatus")
    private Byte accountStatus;

}
