package com.online.yunding.service.impl;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.common.basecurd.service.BaseService;
import com.online.yunding.dao.UserExchangeBalanceDao;
import com.online.yunding.entity.UserExchangeBalance;
import com.online.yunding.server.WebSocketServer;
import com.online.yunding.service.UserExchangeBalanceService;
import com.online.yunding.service.UserIncomeService;
import com.online.yunding.service.UserInfoService;
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
}
