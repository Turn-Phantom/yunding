package com.li.cloud.gateway.security.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.gateway.security.exception.ValidateCodeException;
import com.li.cloud.gateway.security.validate.ValidateCodeProcessor;
import com.li.cloud.gateway.security.validate.domain.ValidateCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/rest/validate")
public class ValidateEntityController {

    @Autowired
    private ValidateCodeProcessor validateCodeProcessor;

    // 校验短信验证码
   @GetMapping("/validateSmsCode")
    public ReturnData validateSmsCode(HttpServletRequest request, String smsCode, String deviceId){
       if(StringUtils.isBlank(smsCode) || StringUtils.isBlank(deviceId)){
           return ReturnData.error("请求参数不能为空");
       }
       request.setAttribute("smsCode", smsCode);
       request.setAttribute("", deviceId);
       try {
           validateCodeProcessor.validate(new ServletWebRequest(request));
       }catch (ValidateCodeException e){
           return ReturnData.error(e.getMessage());
       }
        return ReturnData.success(ReturnData.SUCCESS);
    }
}
