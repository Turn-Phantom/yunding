package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.service.DataDictionaryService;
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
@RequestMapping("/rest/data/dictionary")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /** 根据key获取数据字典下拉数据 */
    @GetMapping("/getDicByKey")
    public ReturnData getDicByKey(String key){
        if(StringUtils.isBlank(key)){
            return ReturnData.error("参数不能为空");
        }
        return ReturnData.successData(dataDictionaryService.queryDicByKey(key));
    }
}
