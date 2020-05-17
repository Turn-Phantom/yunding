package com.online.yunding.service.impl;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.common.basecurd.service.BaseService;
import com.online.yunding.dao.ManageUserDao;
import com.online.yunding.entity.UserAdjustMoney;
import com.online.yunding.service.ManageUserService;
import com.online.yunding.service.UserIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Map;

/**
 * @desc 后台管理用户 逻辑
 * @date 2020-04-30
 */
@Service
public class ManageUserServiceImpl implements ManageUserService {

    @Autowired
    private ManageUserDao manageUserDao;

    @Autowired
    private BaseService baseService;

    @Autowired
    private UserIncomeService userIncomeService;

    /** 查询用户相关数据 */
    @Override
    public Map<String, Object> queryUserRelateInfo() {
        return manageUserDao.queryUserRelateInfo();
    }

    /** 调整用户账户余额 */
    @Override
    public String adjustUserMoney(UserAdjustMoney userAdjustMoney) {
        userAdjustMoney.setAdjustTime(System.currentTimeMillis());
        int insertNum = baseService.insert(userAdjustMoney);
        if(insertNum <= 0){
            return "调整用户余额失败！";
        }
        // 给用户收益表余额调整
        int updateNum = userIncomeService.updateUserIncomeForBalance(userAdjustMoney.getUserId(), userAdjustMoney.getMoney());
        if(updateNum <= 0){
            // 事务回滚
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return "更新用户收益余额表失败";
        }
        return ReturnData.SUCCESS;
    }
}
