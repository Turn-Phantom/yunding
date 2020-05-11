package com.online.yunding.service.impl;

import com.online.yunding.entity.SmsSendRecord;
import com.online.yunding.security.exception.ValidateCodeException;
import com.online.yunding.security.validate.ValidateCodeSender;
import com.online.yunding.service.SmsInterfaceService;
import com.online.yunding.utils.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * @desc 自定义验证码发送器
 * @date 2020-04-26
 */
@Component("validateCodeSender")
public class CusValidateCodeSender implements ValidateCodeSender {

    @Autowired
    private SmsInterfaceService smsInterfaceService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void send(String mobile, String code, Integer smsType,ServletWebRequest request) {
        if(true){
            System.out.println(mobile);
            System.out.println(code);
            return;
        }
        Integer userId;
        try {
            userId = ServletRequestUtils.getIntParameter(request.getRequest(), "");
        } catch (ServletRequestBindingException e) {
            logger.error("非法绑定参数：获取用户id失败");
            throw new ValidateCodeException("parse userId illegal");
        }
        String validCodeTypeStr;
        if(smsType == 1){
            validCodeTypeStr = "注册";
        } else if(smsType == 2){
            validCodeTypeStr = "登录";
        } else if(smsType == 3){
            validCodeTypeStr = "换绑";
            if(null == userId){
                throw new ValidateCodeException("userId is null");
            }
        } else{
            logger.error("非法参数：未知的手机验证码类型" + smsType);
            throw new ValidateCodeException("unKnown sms type");
        }
        String content = String.format(ConstantUtil.SMS_SEND_MSG, validCodeTypeStr, code);
        // 发送验证码
        Map<String, String> sendRet = smsInterfaceService.sendSmsCode(mobile, content);
        if(sendRet.isEmpty()){
            logger.error("验证码发送失败，解析验证码发送结果值为空");
            throw new ValidateCodeException("sendError");
        }
        // 发送成功
        if("Success".equals(sendRet.get("returnstatus"))){
            // 保存发送记录到数据库；并记录到redis中
            saveSendRecord(userId, smsType, mobile);
        } else{
            logger.error("验证码发送失败，请求发送失败：" + sendRet);
            throw new ValidateCodeException("sendError");
        }
    }

    /** 保存发送记录到数据库；并记录到redis中 */
    private void saveSendRecord(Integer userId, Integer smsType, String mobile) {
        SmsSendRecord sendRecord = new SmsSendRecord();
        sendRecord.setId(userId);
        sendRecord.setSendType(Byte.valueOf(smsType+""));
        sendRecord.setSendTime(System.currentTimeMillis());
        sendRecord.setPhone(mobile);
        sendRecord.setSendRes((byte) 1);
        int record = smsInterfaceService.saveSendRecord(sendRecord);
        if(record <= 0){
            logger.error("保存发送记录到数据库失败");
        }
    }
}
