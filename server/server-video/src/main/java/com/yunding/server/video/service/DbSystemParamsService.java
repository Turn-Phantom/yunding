package com.yunding.server.video.service;
import com.yunding.server.video.entity.DbSystemParams;

import java.util.List;
import java.util.Map;

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