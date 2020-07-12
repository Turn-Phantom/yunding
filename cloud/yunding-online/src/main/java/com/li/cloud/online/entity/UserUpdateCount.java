package com.li.cloud.online.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @desc 当天用户信息修改次数记录
 * @date 2020-04-23
 */
@Table(name = "tb_user_update_count")
@Alias("userUpdateCount")
@Setter
@Getter
@Entity
@ToString
public class UserUpdateCount {

    // 用户id； 主键
    @Column(name = "user_id")
    private Integer userId;

    // 手机修改次数；  默认为0
    @Column(name = "phone")
     private Integer phone = 0 ;

    // 密码修改次数； 默认为0
    @Column(name = "password")
    private Integer password = 0;

    // 提款密码修改次数； 默认为0
    @Column(name = "pay_password")
    private Integer payPassword = 0;
}
