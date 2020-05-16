package com.online.yunding.service;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.entity.UserExchangeBalance;

import java.util.List;

/**
 * @desc 申请余额转换
 * @date 2020-05-16
 */
public interface UserExchangeBalanceService {

    /** 分页查询余额转移所有订单 */
    List<UserExchangeBalance> queryExchangeOrderPageList(Pagination<UserExchangeBalance> pagination);

    /** 查询余额转移所有订单 */
    List<UserExchangeBalance> queryExchangeOrderList(Integer userId);

    /** 申请转换金额 */
    String applyExchangeBalance(UserExchangeBalance exchangeBalance);

    /** 根据状态查询转换余额订单数据 */
    List<UserExchangeBalance> queryOrderByStatus(Byte checkStatus);

    /** 审核订单 */
    String checkOrderOperate(UserExchangeBalance userExchangeBalance);
}
