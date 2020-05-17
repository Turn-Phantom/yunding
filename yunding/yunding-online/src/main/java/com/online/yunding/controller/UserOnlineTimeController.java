package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.service.UserOnlineTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 用户在线 控制器
 * @date 2020-05-17
 */
@RestController
@RequestMapping("/rest/user/online")
public class UserOnlineTimeController {

    @Autowired
    private UserOnlineTimeService userOnlineTimeService;

    /** 根据用户id，查询用户在线数据 */
    @GetMapping("/queryOnlineTimeByUserId")
    public ReturnData queryOnlineTimeByUserId(Integer userId){
        if(null == userId){
            return ReturnData.error("用户id不能为空");
        }
        return ReturnData.successData(userOnlineTimeService.queryOnlineTimeByUserId(userId));
    }
}
