package com.online.yunding.service.impl;

import com.online.yunding.dao.ManageUserDao;
import com.online.yunding.service.ManageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @desc 后台管理用户 逻辑
 * @date 2020-04-30
 */
@Service
public class ManageUserServiceImpl implements ManageUserService {

    @Autowired
    private ManageUserDao manageUserDao;

    /** 查询用户相关数据 */
    @Override
    public Map<String, Object> queryUserRelateInfo() {
        return manageUserDao.queryUserRelateInfo();
    }
}
