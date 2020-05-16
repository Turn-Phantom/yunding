package com.online.yunding.security.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.security.validate.ValidateCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc 验证码处理控制器
 * @date 2020-03-22
 */
@RestController
@RequestMapping("/validate/code")
public class ValidateCodeController {

    @Autowired
    private ValidateCodeProcessor validateCodeProcessor;

    /***
     * @desc 发送短信验证码
     *  smsType 验证码类型： 1 注册； 2 登陆； 3 绑定； 4 重置密码
     * @param request 请求体
     * @return void
     * @date 2020-03-22
     */
    @GetMapping("/sendSmsCode")
    public ReturnData sendSmsCode(HttpServletRequest request) throws Exception {
        // 转换请求对象
        ServletWebRequest webRequest = new ServletWebRequest(request);
        // 生成验证码
        validateCodeProcessor.create(webRequest);
        return ReturnData.success("验证码发送成功!");
    }
}
