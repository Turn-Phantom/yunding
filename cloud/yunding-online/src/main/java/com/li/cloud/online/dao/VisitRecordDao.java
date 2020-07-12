package com.li.cloud.online.dao;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.online.entity.VisitRecord;

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
