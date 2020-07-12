package com.li.cloud.queue.dao;

import com.li.cloud.queue.entity.VisitRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc 访问记录 dao接口
 * @date 2020-04-10
 */
public interface VisitRecordDao {

    /** 根据字段查询 */
    List<VisitRecord> queryByField(@Param("visitor") Integer visitor, @Param("visitId") Integer visitId, @Param("visitIp") String visitIp);

    /** 保存访问记录 */
    int updateVisitRecord(VisitRecord visitRecord);

    /** 根据字段删除 */
    int deleteByField(@Param("visitor") Integer visitor, @Param("visitId") Integer visitId, @Param("visitIp") String visitIp);

    /** 将上一日的访问记录表数据，插入到历史表 */
    int insertRecordHisByLastDay();

    /** 根据日期删除访问记录数据 */
    int delByLastDay();

    /** 清除超过15天的登录记录 */
    int clearLoginHis();

    /** 查询前一天的所有历史记录 */
    List<VisitRecord> queryLastDayData();

    /** 保存用户总访问量 */
    int saveVisitTotals(@Param("visitCount") Integer visitCount, @Param("visitType") Integer visitType);
}
