package com.li.cloud.video.service;

/**
 * @desc 视频调度 接口
 * @date 2020-04-18
 */
public interface VideoScheduleService {

    /** 检查异动数据 */
    void checkModifyVideo(String date);

    /** 更新当天数据 */
    void updateCurrVideo(String date);
}
