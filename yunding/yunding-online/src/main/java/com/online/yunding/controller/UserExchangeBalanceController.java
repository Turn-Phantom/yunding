package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.common.basecurd.service.BaseService;
import com.online.yunding.entity.UserExchangeBalance;
import com.online.yunding.service.UserExchangeBalanceService;
import org.apache.commons.lang3.StringUtils;
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

    @Autowired
    private BaseService baseService;

    /** 分页查询余额转移所有订单 */
    @GetMapping("/queryExchangeOrderPageList")
    public ReturnData queryExchangeOrderPageList(Pagination<UserExchangeBalance> pagination){
        userExchangeBalanceService.queryExchangeOrderPageList(pagination);
        return ReturnData.successData(pagination);
    }

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

    /** 根据状态查询转换余额订单数据 */
    @GetMapping("/queryOrderByStatus")
    public ReturnData queryOrderByStatus(Byte checkStatus){
        if(null == checkStatus){
            return ReturnData.error("订单状态参数不能为空！");
        }
        return ReturnData.successData(userExchangeBalanceService.queryOrderByStatus(checkStatus));
    }

    /** 审核订单 */
    @PostMapping("/checkOrderOperate")
    public ReturnData checkOrderOperate(UserExchangeBalance userExchangeBalance){
        if(null == userExchangeBalance.getId() || null == userExchangeBalance.getCheckStatus()){
            return ReturnData.error("操作失败：审核参数不完整");
        }
        if(userExchangeBalance.getCheckStatus() == -1 && StringUtils.isBlank(userExchangeBalance.getRemark())){
            return ReturnData.error("请输入拒绝审核原因！");
        }
        String ret = userExchangeBalanceService.checkOrderOperate(userExchangeBalance);
        if(!ReturnData.SUCCESS.equals(ret)){
            return ReturnData.error(ret);
        }
        return ReturnData.success("操作成功！");
    }
}
