package com.online.yunding.service;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.entity.VisitRecord;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @desc 访问记录 接口
 * @date 2020-04-08
 */
public interface VisitRecordService {

    /** 记录访问信息 */
    String recordVisit(HttpServletRequest request);

    /** 分页查询访问记录数据 */
    List<VisitRecord> queryVisitDataList(Pagination<VisitRecord> pagination);

    /**  查询今日/昨日/总浏览量 */
    Map<String, Object> queryViewCountByPart();
}
