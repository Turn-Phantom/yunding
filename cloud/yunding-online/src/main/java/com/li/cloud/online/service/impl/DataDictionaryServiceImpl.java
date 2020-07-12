package com.li.cloud.online.service.impl;

import com.li.cloud.online.dao.DataDictionaryDao;
import com.li.cloud.online.entity.DataDictionary;
import com.li.cloud.online.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 数据字典 逻辑
 * @date 2020-05-02
 */
@Service
public class DataDictionaryServiceImpl implements DataDictionaryService {

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    /** 根据key查询数据字典 */
    @Override
    public List<DataDictionary> queryDicByKey(String key) {
        return dataDictionaryDao.queryDicByKey(key);
    }
}
