package com.li.cloud.online.dao;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.online.entity.UserExchangeBalance;

import java.util.List;

/**
 * @desc 申请余额转换
 * @date 2020-05-16
 */
public interface UserExchangeBalanceDao {

    /** 分页查询余额转移所有订单 */
    List<UserExchangeBalance> queryExchangeOrderPageList(Pagination<UserExchangeBalance> pagination);

    /** 查询余额转移所有订单 */
    List<UserExchangeBalance> queryExchangeOrderList(Integer userId);

    /** 根据状态查询转换余额订单数据 */
    List<UserExchangeBalance> queryOrderByStatus(Byte checkStatus);
}
