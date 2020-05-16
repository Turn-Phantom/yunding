package com.online.yunding.dao;

import com.online.yunding.entity.UserExchangeBalance;

import java.util.List;

/**
 * @desc 申请余额转换
 * @date 2020-05-16
 */
public interface UserExchangeBalanceDao {

    /** 查询余额转移所有订单 */
    List<UserExchangeBalance> queryExchangeOrderList(Integer userId);
}
