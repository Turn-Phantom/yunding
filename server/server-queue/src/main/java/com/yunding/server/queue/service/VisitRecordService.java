package com.yunding.server.queue.service;

import com.yunding.server.queue.entity.VisitRecord;

/**
 * @desc
 * @date 2020-04-08
 */
public interface VisitRecordService {

    /** 添加访问记录数据到队列 */
    boolean recordVisit(VisitRecord visitRecord);

    /** 将任务队列中的数据，累计到redis中 */
    String pushRecordToRedis();

    /** 执行更新 */
    void executeUpdate(long currTime);

    /** 更新历史记录表 */
    int insertRecordHisByLastDay();

    /** 删除非当天的访问记录 */
    int delVisitRecordByLast();

    /** 清除超过15天的登录记录 */
    int clearLoginHis();
}
