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
 * @desc 短信发送记录
 * @date 2020-04-26
 */
@Table(name = "tb_sms_send_record")
@Alias("smsSendRecord")
@Setter
@Getter
@Entity
@ToString
public class SmsSendRecord {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 用户id
    @Column(name = "user_id")
    private Integer userId;

    // 发送类型：1注册，2 登录，3 换绑
    @Column(name = "send_type")
    private Byte sendType;

    // 发送时间
    @Column(name = "send_time")
    private Long sendTime;

    // 手机号码
    @Column(name = "phone")
    private String phone;

    // 发送结果
    @Column(name = "send_res")
    private byte sendRes;
}
