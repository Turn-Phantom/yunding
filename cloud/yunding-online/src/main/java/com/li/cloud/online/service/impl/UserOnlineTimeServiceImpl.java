package com.li.cloud.online.service.impl;

import com.li.cloud.online.dao.UserOnlineTimeDao;
import com.li.cloud.online.entity.UserOnlineTime;
import com.li.cloud.online.service.UserOnlineTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 用户在线时长 逻辑
 * @date 2020-05-17
 */
@Service
public class UserOnlineTimeServiceImpl implements UserOnlineTimeService {

    @Autowired
    private UserOnlineTimeDao userOnlineTimeDao;

    /** 根据用户id，查询用户收益数据 */
    @Override
    public List<UserOnlineTime> queryOnlineTimeByUserId(Integer userId) {
        return userOnlineTimeDao.queryOnlineTimeByUserId(userId);
    }
}
