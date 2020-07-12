package com.li.cloud.video.service.impl;

import com.li.cloud.video.dao.DbSystemParamsDao;
import com.li.cloud.video.entity.DbSystemParams;
import com.li.cloud.video.service.DbSystemParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @desc 数据库系统参数 实现
 * @date 2020-04-17
 */
@Service
public class DbSystemParamsServiceImpl implements DbSystemParamsService {

    @Autowired
    private DbSystemParamsDao dbSystemParamsDao;

    /** 查询所有系统参数 */
    @Override
    public List<DbSystemParams> queryAllParams() {
        return dbSystemParamsDao.queryAllParams();
    }

    /** 根据字段，查询系统参数 */
    @Override
    public int updateSysParByKey(String parKey){
        return dbSystemParamsDao.updateSysParByKey(parKey);
    }
}
