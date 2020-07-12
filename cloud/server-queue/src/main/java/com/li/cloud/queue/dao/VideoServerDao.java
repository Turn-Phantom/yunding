package com.li.cloud.queue.dao;

import com.li.cloud.queue.entity.UserOnlineTime;
import com.li.cloud.queue.entity.VideoPlayCount;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @desc
 * @date 2020-04-20
 */
public interface VideoServerDao {

    /** 更新影片播放次数 */
    int updateVideoPlayCount(@Param("videoId") String videoId, @Param("count") Integer count);

    /** 查询所有影片播放记录 */
    List<VideoPlayCount> queryAllPlayCount();

    /** 查询当天用户在线记录 */
    UserOnlineTime queryCurrOnlineUser(Integer userId);

    /** 更新用户播放时长 */
    int updateVideoPlayTime(@Param("userId") Integer userId, @Param("count") Integer count);

    /** 清除播记录超过15天的数据 */
    int clearPlayTimeHis();

    /** 将前一天的数据，更新到历史播放时长记录表中 */
    int insertPlayHis();

    /** 在用户播放时长记录表中，删除前一天的数据 */
    int delPlayByLast();

    /** 查询前一天用户播放时长记录 */
    List<UserOnlineTime> queryPlayTimeByLastDay();
}

