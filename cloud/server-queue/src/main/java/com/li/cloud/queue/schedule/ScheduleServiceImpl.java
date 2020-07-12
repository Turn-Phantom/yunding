package com.li.cloud.queue.schedule;

import com.li.cloud.queue.entity.LinkedQueue;
import com.li.cloud.queue.service.UserCalculateService;
import com.li.cloud.queue.service.VideoServerService;
import com.li.cloud.queue.service.VisitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.li.cloud.queue.entity.LinkedQueue.linkQueue;

/**
 * @desc 调度服务实现
 * @date 2020-04-08
 */
@Component
public class ScheduleServiceImpl{

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private VideoServerService videoServerService;

    @Autowired
    private UserCalculateService userCalculateService;

    /** 判断访问记录队列是否为空; */
    @Scheduled(cron = "${schedule.queue.cron}")
    public void isEmptyVisitQueue() {
        int size = linkQueue.size();
        if(size > 0){
            visitRecordService.pushRecordToRedis();
        }
    }

    /** 同步redis中缓存的记录数据到数据库；每五分钟执行一次 */
    @Scheduled(cron = "${schedule.syncDb.cron}")
    public void redisToDataBase(){
        long currTime = System.currentTimeMillis();
        // 改变时间
        LinkedQueue.changeTime = (int) (currTime / 1000);
        // 通过线程执行,更新数据库操作
        visitRecordService.executeUpdate(currTime);
    }

    /** 定时更新历史记录表 */
    @Scheduled(cron = "${schedule.updateHisData.cron}")
    public void updateRecordHis(){
        /* 用户访问历史 */
        // 更新历史记录表数据
        int updateNum = visitRecordService.insertRecordHisByLastDay();
        if(updateNum > 0){
            logger.info("更新历史记录表成功，此次更新数量为：" + updateNum);
        }
        // 删除当天访问记录数据中非当天的数据
        int delNum = visitRecordService.delVisitRecordByLast();
        if(delNum > 0){
            logger.info("删除非当天访问记录成功，删除数量为：" + delNum);
        }

        /* 用户播放时长历史 */
        // 更新播放历史表
        int playHisNum = videoServerService.insertPlayHis();
        if(playHisNum > 0) {
            logger.info("更新用户播放时长历史记录成功，此次更新数量为：" + playHisNum);
        }
        // 更新表异常，不删除数据
        if(playHisNum == -10){
            return;
        }
        // 在用户播放时长记录表中，删除前一天的数据
        int delPlayNum = videoServerService.delPlayByLast();
        if(delPlayNum > 0){
            logger.info("删除非当天播放时长记录成功，删除数量为：" + delPlayNum);
        }

    }

    /** 每天凌晨五点，清除历史数据 */
    @Scheduled(cron = "${schedule.clearHisData.cron}")
    public void clearLoginHis(){
        // 清除登录信息
        int cleanLoginNum = visitRecordService.clearLoginHis();
        if(cleanLoginNum > 0){
            logger.info("清除登录记录超过90天的数据：" + cleanLoginNum);
        }
        // 清除播放统计历史时间
        int clearPlayTimeNum = videoServerService.clearPlayTimeHis();
        if(clearPlayTimeNum > 0){
            logger.info("清除用户播放记录超过90天的数据：" + clearPlayTimeNum);
        }
    }

    /** 统计视频播放次数 */
    @Scheduled(cron = "${schedule.updateVideoPlayCount.cron}")
    public void updateVideoPlayCount(){
        // 更新影片播放次数
        videoServerService.updateVideoPlayCount();
    }

    /** 统计用户播放时长 */
    @Scheduled(cron = "${schedule.videoPlayTimeCount.cron}")
    public void videoPlayTimeCount(){
        // 保存统计时长
        videoServerService.updatePlayTimeCount();
    }

    /** 定时清空用户的修改个人信息记录 */
    @Scheduled(cron = "${schedule.clearUpdateRecord.cron}")
    public void clearUpdateRecord(){
        userCalculateService.clearUserUpdateRecordByLast();
    }

    /** 每隔两个小时查询一次短信验证码剩余次数 */
    @Scheduled(cron = "${schedule.updateSmsSurplus.cron}")
    public void updateSmsSurplus(){
        userCalculateService.querySmsSurplus();
    }

}
