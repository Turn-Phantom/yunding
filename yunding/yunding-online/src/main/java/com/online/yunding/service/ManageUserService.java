package com.online.yunding.service;

import com.online.yunding.entity.UserAdjustMoney;

import java.util.Map;

/**
 * @desc 后台管理用户 接口
 * @date 2020-04-30
 */
public interface ManageUserService {

    /** 查询用户相关数据 */
    Map<String, Object> queryUserRelateInfo();

    /** 调整用户账号余额 */
    String adjustUserMoney(UserAdjustMoney userAdjustMoney);
}
