package com.li.cloud.online.service;

import com.li.cloud.common.basecurd.entity.ReturnData;

import java.util.List;
import java.util.Map;

/**
 * @desc 视频清单数据 接口
 * @date 2020-04-17
 */
public interface VideoDataService extends ConstantData{

    /**
     * @desc 根据类别获取视频清单数据
     * @param cla 类别
     * @param pageNo 页码
     * @param pageSize 每页数量
     * @return
     * @date 2020-04-17
     */
    ReturnData queryVideoListByClass(String cla, Integer pageNo, Integer pageSize);

    /** 根据id获取影片数据 */
    ReturnData getVideoById(String id);

    /** 获取客户端token */
    ReturnData getVideoToken(String videoId);

    /** 获取客户端免费token */
    ReturnData queryFredVideoToken(String videoId);

    /** 获取热播数据 */
    ReturnData queryHotVideo();

    /** 获取影片推荐数据 */
    ReturnData queryRecommendVideo(String videoClass, String currVideoId);

    /** 记录影片播放 */
    void recordVideoPlay(String videoId);

    /** 统计是视频播放时长 */
    void countPlayTime(Integer userId, Integer playTime, Integer countType);

    /** 获取超清，且播放频率最高的10条视频数据 */
    ReturnData queryHeightVideo();

    /** 获取滚动数据 */
    List<Map<String, Object>> queryScrollData();
}
