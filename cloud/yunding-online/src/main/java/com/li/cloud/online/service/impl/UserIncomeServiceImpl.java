package com.li.cloud.online.service.impl;

import com.li.cloud.online.dao.UserIncomeDao;
import com.li.cloud.online.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @desc 用户收益
 * @date 2020-05-16
 */
@Service
public class UserIncomeServiceImpl implements UserIncomeService {

    @Autowired
    private UserIncomeDao userIncomeDao;

    /** 更新用户冻结金额：在原有基础上，增加冻结金额 */
    @Override
    public int updateFreezeMoney(Integer userId, BigDecimal freezeMoney) {
        return userIncomeDao.updateFreezeMoney(userId, freezeMoney);
    }

    /** 更新用户余额 */
    @Override
    public int updateUserIncomeForBalance(Integer userId, BigDecimal balance) {
        return userIncomeDao.updateUserIncomeForBalance(userId, balance);
    }
}
