package com.li.cloud.online.dao;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.online.entity.UserOnlineTime;

import java.util.List;

/**
 * @desc 用户在线时长 dao
 * @date 2020-05-17
 */
public interface UserOnlineTimeDao {

    /** 根据用户id，查询用户在线时长数据 */
    List<UserOnlineTime> queryOnlineTimeByUserId(Pagination<UserOnlineTime> pagination);
}
