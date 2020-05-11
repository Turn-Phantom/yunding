package com.yunding.server.queue.service;

import com.yunding.server.queue.entity.UserIncome;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户相关计算 接口
 * @date 2020-04-21
 */
public interface UserCalculateService {

    /** 根据用户id查询用户信息 */
    UserIncome queryIncomeByUserId(Integer userId);

    /** 更新用户访问量 */
    int updateUserVisitView(Integer userId, Integer visitCount);

    /** 更新用户收益 */
    int updateUserIncome(Integer userId, long onlineSec, BigDecimal incomeTol);

    /** 根据id查询该用户的下级代理用户id */
    List<Integer> queryChildId(int userId);

    /** 清空用户前一天的修改记录 */
    int clearUserUpdateRecordByLast();

    /** 每隔两个小时查询一次短信验证码剩余次数 */
    void querySmsSurplus();
}
