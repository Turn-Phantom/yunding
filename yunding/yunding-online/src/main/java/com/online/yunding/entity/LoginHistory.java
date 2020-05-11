package com.online.yunding.entity;

import cn.hutool.core.clone.CloneSupport;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @desc 用户登录历史记录
 * @date 2020-04-14
 */
@Table(name = "tb_user_login_his")
@Alias("loginHistory")
@Setter
@Getter
@Entity
@ToString
public class LoginHistory extends CloneSupport<LoginHistory> {

    // 主键ID
    @Id
    @Column(name = "id")
    private Integer id;

    // 会员id
    @Column(name = "user_id")
    private Integer userId;

    // 登录ip
    @Column(name = "login_ip")
    private String loginIp;

    // 登录日期
    @Column(name = "login_date")
    private String loginDate;

    // 登录时间
    @Column(name = "login_time")
     private Long loginTime;

    // 登录地址
    private String loginAddr;
}
