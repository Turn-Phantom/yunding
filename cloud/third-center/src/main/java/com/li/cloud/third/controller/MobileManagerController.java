package com.li.cloud.third.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.third.entity.SmsSendRecord;
import com.li.cloud.third.service.SmsInterfaceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @desc 手机管理控制器
 * @date 2020-06-21
 */
@RestController
@RequestMapping("/rest/mobile")
public class MobileManagerController {

    @Autowired
    private SmsInterfaceService smsInterfaceService;

    /** 发送验证码 */
    @PostMapping("/sendSms")
    public ReturnData sendSms(String mobile, String smsContent){
        if(StringUtils.isBlank(mobile) || StringUtils.isBlank(smsContent)){
            return ReturnData.error("third-center: 发送验证码参数不可为空！");
        }
        Map<String, String> retMap = smsInterfaceService.sendSmsCode(mobile, smsContent);
        if(!StringUtils.isEmpty(retMap.get("error"))){
            return ReturnData.error(retMap.get("error"));
        }
        return ReturnData.successData(retMap);
    }

    /** 保存短信发送记录 */
    @PostMapping("/saveSendRecord")
    public ReturnData saveSendRecord(Integer userId, Integer smsType, String mobile){
        SmsSendRecord sendRecord = new SmsSendRecord();
        sendRecord.setId(userId);
        sendRecord.setSendType(Byte.parseByte(smsType+""));
        sendRecord.setSendTime(System.currentTimeMillis());
        sendRecord.setPhone(mobile);
        sendRecord.setSendRes((byte) 1);
        int record = smsInterfaceService.saveSendRecord(sendRecord);
        if(record <= 0){
            return ReturnData.error("保存发送记录到数据库失败");
        }
        return ReturnData.success("保存成功");
    }

    /** 查询短信剩余量 */
    @GetMapping("/querySmsSurplus")
    public ReturnData querySmsSurplus(){
        return ReturnData.successData(smsInterfaceService.querySmsNum());
    }
}
