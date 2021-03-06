package com.online.yunding.dao;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.entity.LoginHistory;
import com.online.yunding.entity.UserInfo;
import com.online.yunding.entity.UserUpdateCount;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @desc 用户信息 dao
 * @date 2020-04-13
 */
public interface UserInfoDao {

    /** 分页查询用户信息 */
    List<UserInfo> queryUserList(Pagination<UserInfo> pagination);

    /** 获取最近15天的登录记录 */
    List<LoginHistory> getLoginHistory(Integer userId);

    /** 通过用户账号查询用户信息 */
    UserInfo queryUserInfoByAccount(String accountNo);

    /** 根据用户id，查询用户信息可更新数据 */
    UserUpdateCount queryUpdateCountByUserId(Integer userId);

    /** 更新修改次数 */
    int updateModifyCount(@Param("field") String field, @Param("userId") Integer userId);

    /** 根据用户id查询推荐人收益 */
    Map<String, Object> queryIncomeByUserId(Integer userId);

    /** 更新用户推荐收益 */
    int updateIncomeByUserId(@Param("recommendMoney") BigDecimal recommendMoney, @Param("userId") Integer userId);
}
