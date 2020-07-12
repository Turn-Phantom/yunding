package com.li.cloud.online.dao;

import com.li.cloud.online.entity.UserIncome;
import com.li.cloud.online.entity.UserOnlineTime;
import com.li.cloud.online.entity.UserTransferAccountOrder;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户订单 dao
 * @date 2020-04-22
 */
public interface UserTransferAccountDao {

    /** 查询用户充值记录 */
    List<UserTransferAccountOrder> queryTransData(@Param("userId") Integer userId, @Param("orderType") byte orderType);

    /** 获取用户可用余额 */
    UserIncome queryUserAvailableBalance(@Param("userId") Integer userId, @Param("account") String accn);

    /** 查询我的收益数据 */
    List<UserOnlineTime> queryIncomeData(@Param("userId") Integer userId, @Param("account") String account);

    /** 冻结用户余额 */
    int freezeMoney(@Param("userId") Integer userId, @Param("transferMoney") BigDecimal transferMoney);

    /** 查询用户充值/提款数据（未审核） */
    List<UserTransferAccountOrder> queryUncheckData();

    /** 根据id查询数据 */
    UserTransferAccountOrder queryDataById(Integer id);

    /** 用户余额表增加余额 */
    int updateUserIncomeForOrder(@Param("id") Integer id, @Param("userId") Integer userId);

    /** 拒绝提款，返还冻结金额 */
    int updateUserIncomeForReject(@Param("userId") Integer userId, @Param("transferMoney") BigDecimal transferMoney);

    /** 云顶后台; 根据用户id，查询用户充值/提款数据 */
    List<UserTransferAccountOrder> queryTransferDataByUserId(Integer userId);

    /** 根据审核状态查询订单数据 */
    List<UserTransferAccountOrder> queryDataByCheckStatus(Byte status);
}
