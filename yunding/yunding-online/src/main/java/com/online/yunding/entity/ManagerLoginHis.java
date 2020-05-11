package com.online.yunding.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc
 * @date 2020-04-26
 */
@Table(name = "tb_manage_login_his")
@Alias("managerLoginHis")
@Setter
@Getter
@Entity
@ToString
public class ManagerLoginHis {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 管理员id
    @Column(name = "manager_id")
    private Integer managerId;

    // 本次登录ip
    @Column(name = "login_ip")
    private String loginIp;

    // 上一次登陆ip
    @Column(name = "last_login_ip")
    private String lastLoginIp;

    // 本次登陆时间
    @Column(name = "login_time")
    private Long loginTime;

    // 上一次登录时间
    @Column(name = "last_login_time")
    private Long lastLoginTime;

}
