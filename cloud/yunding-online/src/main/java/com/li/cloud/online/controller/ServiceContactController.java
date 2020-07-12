package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.online.entity.ServiceContact;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 网站联系 控制器
 * @date 2020-05-19
 */
@RestController
@RequestMapping("/rest/contact")
public class ServiceContactController {

    @Autowired
    private BaseService baseService;

    /** 获取客服qq */
    @GetMapping("/queryServiceQQ")
    public ReturnData queryServiceQQ(){
        ServiceContact serviceContact = new ServiceContact();
        serviceContact.setDataKey("serviceQQ");
        serviceContact = baseService.queryDataByField(serviceContact, "dataKey");
        if(null == serviceContact || StringUtils.isEmpty(serviceContact.getWay())){
            return ReturnData.successData("");
        }
        return ReturnData.successData(serviceContact.getWay());
    }
}
