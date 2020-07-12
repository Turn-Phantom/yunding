package com.li.cloud.tool.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.tool.service.CommonDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 数据字典控制器
 * @date 2020-05-02
 */
@RestController
@RequestMapping("/rest/data/common")
public class CommonDataController {

    @Autowired
    private CommonDataService commonDataService;

    /** 根据key获取数据字典下拉数据 */
    @GetMapping("/querySelByDicType")
    public ReturnData getDicByKey(String dicType){
        if(StringUtils.isBlank(dicType)){
            return ReturnData.error("参数不能为空");
        }
        return ReturnData.successData(commonDataService.querySelByDicType(dicType));
    }
}
