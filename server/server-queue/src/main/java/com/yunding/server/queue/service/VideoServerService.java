package com.yunding.server.queue.service;

import com.yunding.server.queue.constant.ConstantStr;

/**
 * @desc 视频服务 接口
 * @date 2020-04-20
 */
public interface VideoServerService extends ConstantStr {

    /** 更新影片播放次数 */
    int updateVideoPlayCount();

    /** 装载所有影片播放次数到redis中 */
    int loadVideoPlayCount();

    /** 保存用户播放时长*/
    void updatePlayTimeCount();

    /** 清除播记录超过15天的数据 */
    int clearPlayTimeHis();

    /** 将前一天的数据，更新到历史播放时长记录表中 */
    int insertPlayHis();

    /** 在用户播放时长记录表中，删除前一天的数据 */
    int delPlayByLast();
}
