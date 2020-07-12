package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.PasswordUtil;
import com.li.cloud.online.entity.ManagerUser;
import com.li.cloud.online.entity.UserAdjustMoney;
import com.li.cloud.online.entity.UserInfo;
import com.li.cloud.online.service.ManageUserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc
 * @date 2020-04-12
 */
@RestController
@RequestMapping("/rest/manage")
public class ManageUserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BaseService baseService;

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


    /** 查询用户相关数据 */
    @GetMapping("/queryUserRelateInfo")
    public ReturnData queryUserRelateInfo(){
        return ReturnData.successData(manageUserService.queryUserRelateInfo());
    }

    /** 重置用户登录账号 */
    @GetMapping("/resetUserPassword")
    public ReturnData resetUserPassword(UserInfo userInfo){
        if(null == userInfo.getId()){
            return ReturnData.error("用户id不能为空！");
        }
        UserInfo user = baseService.queryById(userInfo);
        String pwd = StringUtils.substring(user.getAccountNo(), user.getAccountNo().length() - 6);
        try {
            userInfo.setPwd(PasswordUtil.encode(pwd));
        } catch (Exception e) {
            logger.error("重置密码失败：", e);
            ReturnData.error("操作失败: 用户密码加密失败！");
        }
        // 修改用户密码
        int updateNum = baseService.updateField(userInfo);
        if(updateNum <= 0){
            return ReturnData.error("重置用户密码失败！");
        }
        return ReturnData.success("重置成功！");
    }

    /** 调整金额 */
    @PostMapping("/adjustMoney")
    public ReturnData adjustMoney(UserAdjustMoney userAdjustMoney){
        if(null == userAdjustMoney){
            return ReturnData.error("对象参数为空");
        }
        if(null == userAdjustMoney.getUserId() || null == userAdjustMoney.getMoney() || StringUtils.isBlank(userAdjustMoney.getRemark())){
            return ReturnData.error("请求参数不完整");
        }
        String ret = manageUserService.adjustUserMoney(userAdjustMoney);
        if(!ReturnData.SUCCESS.equals(ret)){
            return ReturnData.error(ret);
        }
        return ReturnData.success("调整成功！");
    }
}
