package com.li.cloud.tool.service;

import com.li.cloud.tool.entity.DataDictionary;

import java.util.List;

/**
 * @desc 数据字典 接口
 * @date 2020-05-02
 */
public interface CommonDataService {

    /** 根据key查询数据字典 */
    List<DataDictionary> querySelByDicType(String dicType);
}
