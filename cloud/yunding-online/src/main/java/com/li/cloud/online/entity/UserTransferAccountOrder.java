package com.li.cloud.online.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * @desc
 * @date 2020-04-22
 */
@Table(name = "tb_user_transfer_accn")
@Alias("userTransferAccountOrder")
@Setter
@Getter
@Entity
@ToString
public class UserTransferAccountOrder {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 用户ID
    @Column(name = "user_id")
    private Integer userId;

    // 转账类型：0提款， 1充值
    @Column(name = "order_type")
    private Byte orderType;

    // 转账人
    @Column(name = "transfer_name")
    private String transferName;

    // 转账银行名称
    @Column(name = "transfer_bank_name")
    private String transferBankName;

    // 转账账号
    @Column(name = "transfer_account")
    private String transferAccount;

    // 转账金额
    @Column(name = "transfer_money")
    private BigDecimal transferMoney;

    // 转账类型
    @Column(name = "transfer_type")
    private String transferType;

    // 转账日期
    @Column(name = "transfer_date")
    private String transferDate;

    // 订单生成日期
    @Column(name = "create_time")
    private Long createTime;

    // 审核状态：0 未审核； 1 已审核；-1 订单异常
    @Column(name = "check_status")
    private Byte checkStatus;

    // 审核时间
    @Column(name = "check_time")
    private Long checkTime;

    // 订单异常原因描述
    @Column(name = "exception_msg")
    private String exceptionMsg;

    // 备注
    @Column(name = "remark")
    private String remark;

    // 转账密码（用户信息中的提款密码）
    @Transient
    private String transferPassword;

    private BigDecimal currBalance;

    // 用户账号
    private String accountNo;
}
