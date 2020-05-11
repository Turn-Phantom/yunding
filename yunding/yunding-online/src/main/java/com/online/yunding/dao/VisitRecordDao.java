package com.online.yunding.dao;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.entity.VisitRecord;

import java.util.List;
import java.util.Map;

/**
 * @desc 浏览记录 dao
 * @date 2020-05-02
 */
public interface VisitRecordDao {

    /** 分页查询访问记录数据 */
    List<VisitRecord> queryVisitDataList(Pagination<VisitRecord> pagination);

    /**  查询今日/昨日/总浏览量 */
    Map<String,Object> queryViewCountByPart();
}
