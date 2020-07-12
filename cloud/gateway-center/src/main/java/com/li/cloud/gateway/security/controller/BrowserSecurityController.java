package com.li.cloud.gateway.security.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @desc: 浏览器安全控制器类， 处理控制器登录页面跳转等
 * @date: 2019-11-15
 */
@RestController
@RequestMapping(value = "/authentication")
public class BrowserSecurityController {

    /**
     * @desc: 处理身份认证
     *      当需要认证身份时，跳转到该方法处理
     * @param:
     * @return:
     * @date: 2019-11-15
     */
    @RequestMapping(value = "/require")
    @ResponseStatus(HttpStatus.UNAUTHORIZED) // 返回未授权的状态码
    public ReturnData requireAuthenticationAction(HttpServletRequest request, HttpServletResponse response){
        return ReturnData.unLogin("当前为游客状态，请先登录！");
    }
}
