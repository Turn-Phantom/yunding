package com.li.cloud.tool.dao;

import com.li.cloud.tool.entity.DataDictionary;

import java.util.List;

/**
 * @desc 数据字典 dao
 * @date 2020-05-02
 */
public interface CommonDataDao {

    /** 根据key查询数据字典 */
    List<DataDictionary> querySelByDicType(String dicType);
}
