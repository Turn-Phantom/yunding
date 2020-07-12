package com.li.cloud.online.service;

import com.li.cloud.online.entity.UserOnlineTime;

import java.util.List;

/**
 * @desc 用户在线时长 接口
 * @date 2020-05-17
 */
public interface UserOnlineTimeService {

    /** 根据用户id，查询用户在线时长 */
    List<UserOnlineTime> queryOnlineTimeByUserId(Integer userId);
}
