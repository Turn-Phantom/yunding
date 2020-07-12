package com.li.cloud.online.service.impl;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.online.dao.UserTransferAccountDao;
import com.li.cloud.online.entity.UserIncome;
import com.li.cloud.online.entity.UserOnlineTime;
import com.li.cloud.online.entity.UserTransferAccountOrder;
import com.li.cloud.online.service.UserTransferAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户余额 逻辑
 * @date 2020-04-22
 */
@Service
public class UserTransferAccountServiceImpl implements UserTransferAccountService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserTransferAccountDao userTransferAccountDao;

    @Autowired
    private BaseService baseService;

    /** 查询我的收益数据 */
    @Override
    public List<UserOnlineTime> queryIncomeData(Integer id, String account) {
        return userTransferAccountDao.queryIncomeData(id,  account);
    }

    /** 查询用户充值记录 */
    @Override
    public List<UserTransferAccountOrder> queryTransData(Integer userId, Byte orderType) {
        return userTransferAccountDao.queryTransData(userId, orderType);
    }

    /** 获取用户可用余额 */
    @Override
    public BigDecimal queryUserAvailableBalance(Integer userId, String accn) {
        UserIncome userIncome = userTransferAccountDao.queryUserAvailableBalance(userId, accn);
        if(null == userIncome){
            return BigDecimal.ZERO;
        }
        return userIncome.getIncomeTotal().add(userIncome.getBalance()).add(userIncome.getRecommendMoney()).subtract(userIncome.getFreezeMoney());
    }

    /** 冻结用户余额 */
    @Override
    public int freezeMoney(Integer userId, BigDecimal transferMoney) {
        return userTransferAccountDao.freezeMoney(userId, transferMoney);
    }

    /** 查询用户充值/提款数据（未审核） */
    @Override
    public List<UserTransferAccountOrder> queryUncheckData() {
        return userTransferAccountDao.queryUncheckData();
    }

    /** 根据id查询数据 */
    @Override
    public UserTransferAccountOrder queryDataById(Integer id) {
        return userTransferAccountDao.queryDataById(id);
    }

    /** 通过充值/提款申请 */
    @Override
    public String updatePassOrder(Integer id, Integer userId, String remark) {
        // 更改数据库状态
        UserTransferAccountOrder order = new UserTransferAccountOrder();
        order.setId(id);
        order.setCheckStatus((byte) 1);
        order.setCheckTime(System.currentTimeMillis());
        order.setRemark(remark);
        int updateNum = baseService.updateField(order);
        if(updateNum <= 0){
            logger.error("更新数数据失败, 未成功更新订单申请状态, 更新返回影响行数为0");
            return "更新数数据失败, 未成功更新订单申请状态";
        }
        // 根据id查询数据
        UserTransferAccountOrder accountOrder = baseService.queryById(order);
        // 充值
        if(accountOrder.getOrderType() == 1){
            // 用户余额表增加余额
            int res = userTransferAccountDao.updateUserIncomeForOrder(id, userId);
            if(res <= 0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "更新用户余额失败，未成功通过审核！";
            }
        }

        return ReturnData.SUCCESS;
    }

    /** 拒绝充值/提款申请 */
    @Override
    public String updateRejectOrder(Integer id, Integer userId, String ret) {
        UserTransferAccountOrder order = new UserTransferAccountOrder();
        order.setId(id);
        order.setExceptionMsg(ret);
        order.setCheckTime(System.currentTimeMillis());
        order.setCheckStatus((byte) -1);
        int updateNum = baseService.updateField(order);
        if(updateNum <= 0){
            logger.error("更新审核结果失败！更新返回影响数为0");
            return "操作失败！";
        }
        // 拒绝提款，返还冻结金额
        UserTransferAccountOrder accountOrder = baseService.queryById(order);
        if(accountOrder.getOrderType() == 0){
            // 释放冻结金额
            int retNum = userTransferAccountDao.updateUserIncomeForReject(userId, accountOrder.getTransferMoney());
            if(retNum <= 0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                logger.error("拒绝审核操作失败，释放用户冻结金额失败！");
                return "操作失败，未成功释放冻结金额";
            }
        }
        return ReturnData.SUCCESS;
    }

    /** 云顶后台; 根据用户id，查询用户充值/提款数据 */
    @Override
    public List<UserTransferAccountOrder> queryTransferDataByUserId(Integer userId) {
        return userTransferAccountDao.queryTransferDataByUserId(userId);
    }

    /** 根据审核状态查询订单数据 */
    @Override
    public List<UserTransferAccountOrder> queryDataByCheckStatus(Byte status) {
        return userTransferAccountDao.queryDataByCheckStatus(status);
    }
}
