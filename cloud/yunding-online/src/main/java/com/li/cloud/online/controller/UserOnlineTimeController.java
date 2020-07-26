package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.online.entity.UserOnlineTime;
import com.li.cloud.online.service.UserOnlineTimeService;
import org.apache.commons.lang3.StringUtils;
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
    public ReturnData queryOnlineTimeByUserId(Pagination<UserOnlineTime> pagination){

        if(StringUtils.isBlank((CharSequence) pagination.getParams().get("userId"))){
            return ReturnData.error("用户id不能为空");
        }

        userOnlineTimeService.queryOnlineTimeByUserId(pagination);

        return ReturnData.successData(pagination);
    }
}
