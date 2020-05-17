package com.online.yunding.dao;

import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

/**
 * @desc 用户收益 dao
 * @date 2020-05-16
 */
public interface UserIncomeDao {

    /** 更新用户冻结金额：在原有基础上，增加冻结金额 */
    int updateFreezeMoney(@Param("userId") Integer userId, @Param("freezeMoney") BigDecimal freezeMoney);

    /** 更新用户余额 */
    int updateUserIncomeForBalance(@Param("userId") Integer userId, @Param("balance") BigDecimal balance);
}
