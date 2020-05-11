package com.online.yunding.dao;

import com.online.yunding.entity.DataDictionary;

import java.util.List;

/**
 * @desc 数据字典 dao
 * @date 2020-05-02
 */
public interface DataDictionaryDao {

    /** 根据key查询数据字典 */
    List<DataDictionary> queryDicByKey(String key);
}
