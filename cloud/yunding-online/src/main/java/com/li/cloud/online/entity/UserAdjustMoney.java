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
 * @desc 用户金额调整
 * @date 2020-05-17
 */
@Table(name = "tb_user_adjust_money")
@Alias("userAdjustMoney")
@Setter
@Getter
@Entity
@ToString
public class UserAdjustMoney {

    // 主键
    @Id
    @Column(name = "id")
    private Integer id;

    // 用户id
    @Column(name = "user_id")
    private Integer userId;

    // 调整金额
    @Column(name = "money")
    private BigDecimal money;

    // 调整时间
    @Column(name = "adjust_time")
    private Long adjustTime;

    // 备注
    @Column(name = "remark")
    private String remark;
}
