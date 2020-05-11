package com.online.yunding.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @desc 云顶娱乐同步信息
 * @date 2020-04-05
 */
@Table(name = "tb_yd_vipInfo")
@Alias("ydVipInfo")
@Setter
@Getter
@Entity
@ToString
public class YdVipInfo {

    // 主键id
    @Id
    @Column(name = "id")
    private Integer id;

    // 会员ID
    @Column(name = "vipNo")
    private Integer vipNo;

    // 云顶娱乐会员推荐ID
    @Column(name = "yd_vipId")
    private String ydVipId;

    // 云顶娱乐平台会员积分
    @Column(name = "yd_vipPoints")
    private BigDecimal ydVipPoints;

    // 同步云顶娱乐平台状态：默认为成功；1成功，0失败
    @Column(name = "sync_yunding")
    private Byte syncYunding;

    // 同步云顶错误消息
    @Column(name = "sync_errMsg")
    private String syncErrMsg;

    // 同步云顶密码； -1 未同步; 1 已同步
    @Column(name = "sync_pwd")
    private Byte syncPwd;

    // 同步云顶取款密码， -1 未同步； 1已同步
    @Column(name = "sync_pay_pwd")
    private Byte syncPayPwd;
}
