package com.online.yunding.dao;

import com.online.yunding.entity.UserOnlineTime;

import java.util.List;

/**
 * @desc 用户在线时长 dao
 * @date 2020-05-17
 */
public interface UserOnlineTimeDao {

    /** 根据用户id，查询用户在线时长数据 */
    List<UserOnlineTime> queryOnlineTimeByUserId(Integer userId);
}
