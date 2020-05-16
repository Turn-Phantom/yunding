package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.entity.UserExchangeBalance;
import com.online.yunding.service.UserExchangeBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @desc 申请余额转换
 * @date 2020-05-16
 */
@RestController
@RequestMapping("/rest/user/balance")
public class UserExchangeBalanceController {

    @Autowired
    private UserExchangeBalanceService userExchangeBalanceService;

    /** 查询余额转移所有订单 */
    @GetMapping("/queryExchangeOrderList")
    public ReturnData queryExchangeOrderList(Integer userId){
        if(null == userId){
            return ReturnData.error("用户id不能为空！");
        }
        return ReturnData.successData(userExchangeBalanceService.queryExchangeOrderList(userId));
    }

    /** 申请用户余额转移订单 */
    @PostMapping("/exchangeBalance")
    public ReturnData exchangeBalance(UserExchangeBalance exchangeBalance){
        if(null == exchangeBalance){
            return ReturnData.error("申请失败");
        }
        if(null == exchangeBalance.getUserId()){
            return ReturnData.error("申请失败");
        }
        if(null == exchangeBalance.getMoney() || exchangeBalance.getMoney() == 0){
            return ReturnData.error("转移余额必须是大于0的数字");
        }
        String ret = userExchangeBalanceService.applyExchangeBalance(exchangeBalance);
        if(!ReturnData.SUCCESS.equals(ret)){
            return ReturnData.error(ret);
        }
        return ReturnData.success("提交申请成功");
    }
}
