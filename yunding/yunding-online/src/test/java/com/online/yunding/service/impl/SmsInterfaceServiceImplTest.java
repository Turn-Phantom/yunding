package com.online.yunding.service.impl;

import com.online.yunding.entity.SmsSendRecord;
import com.online.yunding.service.SmsInterfaceService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SmsInterfaceServiceImplTest {

    @Autowired
    private SmsInterfaceService smsInterfaceService;

    /** 查询短信剩余数 */
    @Test
    public void querySmsSurplus(){
        System.out.println(smsInterfaceService.querySmsNum());
    }

    /** 发送短信 */
    @Test
    public void sendMessage(){
        smsInterfaceService.sendSmsCode("19865762309", "【云顶】您的验证码为：12345，请不要把验证码泄露给其他人！5分钟内有效");
    }

    /** 保存发送记录 */
    @Test
    public void saveRecord(){
        SmsSendRecord sendRecord = new SmsSendRecord();
        sendRecord.setUserId(null);
        sendRecord.setSendType((byte) 1);
        sendRecord.setSendTime(System.currentTimeMillis());
        sendRecord.setPhone("19865762309");
        sendRecord.setSendRes((byte) 1);
        int record = smsInterfaceService.saveSendRecord(sendRecord);
        System.out.println(record);
    }

    @Test
    public void querySmsSurplusLow(){
        Map<String, String> map = new HashMap<>();
        map.put("overage", "49");
    }

}