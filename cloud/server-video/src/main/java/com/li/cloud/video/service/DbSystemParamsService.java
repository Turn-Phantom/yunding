package com.li.cloud.video.service;
import com.li.cloud.video.entity.DbSystemParams;

import java.util.List;

/**
 * @desc 数据库系统参数 接口
 * @date 2020-04-17
 */
public interface DbSystemParamsService {

    /** 查询所有系统参数 */
    List<DbSystemParams> queryAllParams();

    /** 根据字段，查询系统参数 */
    int updateSysParByKey(String parKey);
}