package com.li.cloud.online.service.impl;

import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.online.dao.UserExchangeBalanceDao;
import com.li.cloud.online.entity.UserExchangeBalance;
import com.li.cloud.online.server.WebSocketServer;
import com.li.cloud.online.service.UserExchangeBalanceService;
import com.li.cloud.online.service.UserIncomeService;
import com.li.cloud.online.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 申请余额转换
 * @date 2020-05-16
 */
@Service
public class UserExchangeBalanceServiceImpl implements UserExchangeBalanceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserExchangeBalanceDao userExchangeBalanceDao;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserIncomeService userIncomeService;

    @Autowired
    private WebSocketServer webSocketServer;

    /** 分页查询余额转移所有订单 */
    @Override
    @CustomPage
    public List<UserExchangeBalance> queryExchangeOrderPageList(Pagination<UserExchangeBalance> pagination) {
        return userExchangeBalanceDao.queryExchangeOrderPageList(pagination);
    }

    /** 查询余额转移所有订单 */
    @Override
    public List<UserExchangeBalance> queryExchangeOrderList(Integer userId) {
        return userExchangeBalanceDao.queryExchangeOrderList(userId);
    }

    /** 申请转换金额 */
    @Override
    public String applyExchangeBalance(UserExchangeBalance exchangeBalance) {
        // 根据用户id 查询用户金额
        BigDecimal incomeBalance = userInfoService.queryUserIncomeBalance(exchangeBalance.getUserId());
        if(incomeBalance.compareTo(new BigDecimal(String.valueOf(exchangeBalance.getMoney()))) < 0){
            return "对不起，您的余额不足，请刷新页面重新操作";
        }
        exchangeBalance.setCheckStatus((byte) 0);
        exchangeBalance.setApplyTime(System.currentTimeMillis());
        int insertNum = baseService.insert(exchangeBalance);
        if(insertNum <= 0){
            return "申请失败！";
        }
        // 申请成功后，冻结金额
        int updateNum = userIncomeService.updateFreezeMoney(exchangeBalance.getUserId(), new BigDecimal(exchangeBalance.getMoney()));
        if(updateNum <= 0){
            // 回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "申请失败，冻结金额失败！";
        }
        try {
            webSocketServer.sendMessage("用户：" + exchangeBalance.getUserId() + "，申请余额转移：" + exchangeBalance.getMoney() + "元，待审核");
        } catch (Exception e) {
            logger.error("用户ID：" + exchangeBalance.getUserId() + "，申请转移通知失败：", e);
        }
        return ReturnData.SUCCESS;
    }

    /** 根据状态查询转换余额订单数据 */
    @Override
    public List<UserExchangeBalance> queryOrderByStatus(Byte checkStatus) {
        return userExchangeBalanceDao.queryOrderByStatus(checkStatus);
    }

    /** 审核订单 */
    @Override
    public String checkOrderOperate(UserExchangeBalance userExchangeBalance) {
        userExchangeBalance.setCheckTime(System.currentTimeMillis());
        int updateNum = baseService.updateField(userExchangeBalance);
        if(updateNum <= 0){
            return "更新失败！";
        }
        // 拒绝成功，解冻用户金额
        if(userExchangeBalance.getCheckStatus() == -1){
            // 根据id，查询订单数据
            UserExchangeBalance balanceOrder = baseService.queryById(userExchangeBalance);
            int upNum = userIncomeService.updateFreezeMoney(balanceOrder.getUserId(), new BigDecimal(balanceOrder.getMoney()).negate());
            if(upNum <= 0){
                // 回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return "操作失败：解冻金额失败！";
            }
        }
        return ReturnData.SUCCESS;
    }
}
