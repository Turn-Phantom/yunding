package com.online.yunding.service;

import java.math.BigDecimal;

/**
 * @desc 用户收益
 * @date 2020-05-16
 */
public interface UserIncomeService {

    /** 更新用户冻结金额：在原有基础上，增加冻结金额 */
    int updateFreezeMoney(Integer userId, BigDecimal freezeMoney);

    /** 更新用户余额 */
    int updateUserIncomeForBalance(Integer userId, BigDecimal balance);
}
