package com.li.cloud.tool.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.tool.service.MobileMsgService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 手机短信控制器
 * @date 2020-05-06
 */
@RestController
@RequestMapping("/rest/sms/send")
public class MobileMsgController {

    @Autowired
    private MobileMsgService mobileMsgService;

    /** 查询未发送过的手机号码列表 */
    @GetMapping("/queryPhoneNoList")
    public ReturnData queryPhoneNoList(Integer count){
        if(count == null){
            return ReturnData.error("读取失败：读取条数为空");
        }
        return ReturnData.successData(mobileMsgService.queryPhoneNoList(count));
    }

    /** 更新手机发送状态 */
    @PostMapping("/updatePhoneStatus")
    public void updatePhoneStatus(String phoneNum, Byte status){
        mobileMsgService.updatePhoneStatus(phoneNum, status);
    }

    /** 清空手机缓存号码 */
    @PostMapping("/cleanCacheNumber")
    public ReturnData cleanCacheNumber(String numbers){
        if(StringUtils.isBlank(numbers)){
            return ReturnData.error("参数数据为空");
        }
        String ret = mobileMsgService.cleanCacheNumber(numbers);
        if(!ReturnData.SUCCESS.equals(ret)){
            ReturnData.error(ret);
        }
        return ReturnData.success("清除成功");
    }
}
