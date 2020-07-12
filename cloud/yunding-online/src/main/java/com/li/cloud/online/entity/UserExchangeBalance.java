package com.li.cloud.online.entity;

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
 * @desc 用户转换余额（申请余额到云顶集团）
 * @date 2020-05-16
 */
@Table(name = "tb_user_exc_balance")
@Alias("userExchangeBalance")
@Setter
@Getter
@Entity
@ToString
public class UserExchangeBalance {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 用户id
    @Column(name = "user_id")
    private Integer userId;

    // 转移金额
    @Column(name = "money")
    private Integer money;

    // 申请时间
    @Column(name = "apply_time")
    private Long applyTime;

    // 处理状态：0 处理中，1 已审核， -1 拒绝申请
    @Column(name = "check_status")
    private Byte checkStatus;

    // 处理时间
    @Column(name = "check_time")
    private Long checkTime;

    // 备注
    @Column(name = "remark")
    private String remark;

    private String userAccount;
}
