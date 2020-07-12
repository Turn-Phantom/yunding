package com.li.cloud.online.service;

import com.li.cloud.online.entity.UserOnlineTime;
import com.li.cloud.online.entity.UserTransferAccountOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户余额 接口
 * @date 2020-04-22
 */
public interface UserTransferAccountService {


    /** 查询我的收益数据 */
    List<UserOnlineTime> queryIncomeData(Integer id, String account);

    /** 查询用户充值记录 */
    List<UserTransferAccountOrder> queryTransData(Integer userId, Byte orderType);

    /** 获取用户可用余额 */
    BigDecimal queryUserAvailableBalance(Integer id, String accn);

    /** 冻结用户余额 */
    int freezeMoney(Integer userId, BigDecimal transferMoney);

    /** 查询用户充值/提款数据（未审核） */
    List<UserTransferAccountOrder> queryUncheckData();

    /** 根据id查询数据 */
    UserTransferAccountOrder queryDataById(Integer id);

    /** 通过充值/提款申请 */
    String updatePassOrder(Integer id, Integer userId, String remark);

    /** 拒绝充值/提款申请 */
    String updateRejectOrder(Integer id, Integer userId, String ret);

    /** 云顶后台; 根据用户id，查询用户充值/提款数据 */
    List<UserTransferAccountOrder> queryTransferDataByUserId(Integer userId);

    /** 根据审核状态查询订单数据 */
    List<UserTransferAccountOrder> queryDataByCheckStatus(Byte status);
}
