package com.li.cloud.online.dao;

import java.util.Map;

/**
 * @desc 后台管理用户 dao
 * @date 2020-04-30
 */
public interface ManageUserDao {

    /** 查询用户相关数据 */
    Map<String,Object> queryUserRelateInfo();
}
