package com.li.cloud.video.schedule;

import com.li.cloud.common.utils.DateUtil;
import com.li.cloud.video.service.VideoScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @desc 视频调度 实现
 * @date 2020-04-18
 */
@Component
public class ServerScheduleImpl {

    @Autowired
    private VideoScheduleService videoScheduleService;

    /** 每天凌晨4点与晚上10点，更新一次影片资料；内容：当天异动与当天最新资料； 说明：凌晨四点更新前一天的数据 */
    @Scheduled(cron="${schedule.videoUpdate.cron}")
    public void updateVideoForCurrDate(){
        String utcDate;
        boolean isAm = cn.hutool.core.date.DateUtil.isAM(new Date());
        // 早上，操作昨天的数据, 否则操作当日的数据
        if(isAm){
            utcDate = DateUtil.getUTCDate("yyyyMMdd", -1L);
        } else{
            utcDate = DateUtil.getUTCDate("yyyyMMdd");
        }
        // 检查异动数据
        videoScheduleService.checkModifyVideo(utcDate);
        // 更新数据
        videoScheduleService.updateCurrVideo(utcDate);
    }
}
