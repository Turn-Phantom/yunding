package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.common.basecurd.service.BaseService;
import com.online.yunding.entity.ManagerUser;
import com.online.yunding.service.ManageUserService;
import com.online.yunding.service.SmsInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc
 * @date 2020-04-12
 */
@RestController
@RequestMapping("/rest/manage")
public class ManageUserController {

    @Autowired
    private BaseService baseService;

    @Autowired
    private SmsInterfaceService smsInterfaceService;

    @Autowired
    private ManageUserService manageUserService;

    /** 根据账号获取用户信息 */
    @GetMapping("/getManageByAccount")
    public ReturnData getManageByAccount(String account){
        if(StringUtils.isBlank(account)){
            return ReturnData.error("参数不能位空！");
        }
        ManagerUser managerUser = new ManagerUser();
        managerUser.setAccount(account);
        return ReturnData.successData(baseService.queryDataByField(managerUser, "account"));
    }

    /** 查询短信剩余量 */
    @GetMapping("/querySmsSurplus")
    public ReturnData querySmsSurplus(){
        return ReturnData.successData(smsInterfaceService.querySmsNum());
    }

    /** 查询用户相关数据 */
    @GetMapping("/queryUserRelateInfo")
    public ReturnData queryUserRelateInfo(){
        return ReturnData.successData(manageUserService.queryUserRelateInfo());
    }
}
