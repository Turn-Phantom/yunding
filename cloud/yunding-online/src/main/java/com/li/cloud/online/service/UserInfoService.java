package com.li.cloud.online.service;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.online.entity.LoginHistory;
import com.li.cloud.online.entity.UserInfo;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @desc 用户信息 逻辑
 * @date 2020-03-31
 */
public interface UserInfoService {

    /** 用户注册 */
    String register(Map<String, String> registerData, HttpServletRequest request);

    /** 记录用户登录痕迹 */
    String saveLoginHis(Integer userId, String loginIp);

    /** 分页获取用户信息 */
    List<UserInfo> queryUserList(Pagination<UserInfo> pagination);

    /** 获取最近15天的登录记录 */
    List<LoginHistory> getLoginHistory(Pagination<LoginHistory> pagination);

    /**
     * @desc 修改用户信息
     * @param userInfo 用户信息实体
     * @param field 修改的字段
     * @date 2020-04-15
     */
    String updateUserInfo(UserInfo userInfo, String field, HttpServletRequest request);

    /** 通过用户账号查询用户信息 */
    UserInfo queryUserInfoByAccount(String accountNo);

    /** 根据用户id，增加用户金额 */
    void addBalance(Integer userId);

    /** 短信验证码校验 */
    String smsCodeValidate(String validateCode, ServletWebRequest webRequest);

    /** 根据手机号码更新用户密码 */
    int updateUserInfoByPhoneNo(String password, String phoneNum);

    /** 根据用户id，查询用户收益 */
    BigDecimal queryUserIncomeBalance(Integer userId);

    /** 换绑用户手机号码*/
    String handsetChange(UserInfo userInfo);
}
