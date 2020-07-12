package com.li.cloud.tool.service.impl;

import com.li.cloud.tool.dao.CommonDataDao;
import com.li.cloud.tool.entity.DataDictionary;
import com.li.cloud.tool.service.CommonDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 公共数据 逻辑
 * @date 2020-05-02
 */
@Service
public class CommonDataServiceImpl implements CommonDataService {

    @Autowired
    private CommonDataDao commonDataDao;

    /** 根据key查询数据字典 */
    @Override
    public List<DataDictionary> querySelByDicType(String dicType) {
        return commonDataDao.querySelByDicType(dicType);
    }
}
