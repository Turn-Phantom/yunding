package com.li.cloud.gateway.security.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 安全控制器
 * @date 2020-04-05
 */
@RestController
@RequestMapping("/rest/auth")
public class SecurityController {

    /**
     * @desc 验证token是否可用
     * @param currMs 当前时间
     * @return
     * @date 2020-04-05
     */
    @GetMapping("/isInvalid")
    public ReturnData validateToken(Long currMs){
        return ReturnData.successData(currMs);
    }

    @PostMapping("/test")
    public ReturnData test(){
        return ReturnData.successData("success");
    }
}
