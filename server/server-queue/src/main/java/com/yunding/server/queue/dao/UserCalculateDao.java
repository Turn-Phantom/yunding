package com.yunding.server.queue.dao;

import com.yunding.server.queue.entity.UserIncome;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * @desc 用户计算相关 dao
 * @date 2020-04-21
 */
public interface UserCalculateDao {

    /** 根据用户id查询用户信息 */
    UserIncome queryIncomeByUserId(Integer userId);

    /** 更新用户访问量 */
    int updateUserVisitView(@Param("userId") Integer userId, @Param("visitCount") Integer visitCount);

    /** 更新用户收益 */
    int updateUserIncome(@Param("userId") Integer userId, @Param("onlineSec") long onlineSec, @Param("incomeTol") BigDecimal incomeTol);

    /** 根据id查询该用户的下级代理用户id */
    List<Integer> queryChildId(int userId);

    /** 清空用户前一天的修改记录 */
    int clearUserUpdateRecordByLast();
}
