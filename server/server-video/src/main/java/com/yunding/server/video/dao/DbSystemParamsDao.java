package com.yunding.server.video.dao;

import com.yunding.server.video.entity.DbSystemParams;

import java.util.List;

/**
 * @desc 数据库系统参数
 * @date 2020-04-17
 */
public interface DbSystemParamsDao {

    /** 查询所有系统参数 */
    List<DbSystemParams> queryAllParams();

    /** 根据参数key，更新系统参数 */
    int updateSysParByKey(String parKey);
}
