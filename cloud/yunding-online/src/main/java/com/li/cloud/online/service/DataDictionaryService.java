package com.li.cloud.online.service;

import com.li.cloud.online.entity.DataDictionary;

import java.util.List;

/**
 * @desc 数据字典 接口
 * @date 2020-05-02
 */
public interface DataDictionaryService {

    /** 根据key查询数据字典 */
    List<DataDictionary> queryDicByKey(String key);
}
