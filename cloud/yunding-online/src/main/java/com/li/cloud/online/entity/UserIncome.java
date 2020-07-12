package com.li.cloud.online.entity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * @desc 用户收益
 * @date 2020-04-21
 */
@Table(name = "tb_user_income")
@Alias("userIncome")
@Setter
@Getter
@Entity
@ToString
public class UserIncome {

    // 用户ID
    @Column(name = "user_id")
    private Integer userId;

    // 总在线时长
    @Column(name = "online_time_total")
    private Long onlineTimeTotal;

    // 总在线收益
    @Column(name = "income_total")
     private BigDecimal incomeTotal;

    // 充值余额
    @Column(name = "balance")
    private BigDecimal balance;

    // 推荐人余额
    @Column(name = "recommend_money")
    private BigDecimal recommendMoney;

    // 冻结余额
    @Column(name = "freeze_money")
    private BigDecimal freezeMoney;

    // 总访问量
    @Column(name = "visit_count")
    private Integer visitCount;

    // 备注说明
    @Column(name = "income_desc")
    private String incomeDesc;
}
