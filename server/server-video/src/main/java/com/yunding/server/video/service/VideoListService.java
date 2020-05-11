package com.yunding.server.video.service;


import com.yunding.server.common.basecurd.entity.Pagination;
import com.yunding.server.video.entity.VideoList;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @desc 影片清单 业务接口
 * @date 2020-04-16
 */
public interface VideoListService {

    /** 从第三方汇入影片清单 */
    int insertVideoList();

    /** 根据类别获取分页视频清单数据 */
    List<VideoList> queryPageByClass(Pagination<VideoList> pagination);

    /** 保存影片操作日志 */
    void insertOperateLog(String pos, String content, String res);

    /** 根据视频id获取视频清单 */
    List<VideoList> queryVideoList(Set<String> videoId);

    /** 获取影片token */
    Map<String, Object> queryVideoToken(String flag, String videoId);

    /** 获取热播数据 */
    List<VideoList> queryHotVideo();

    /** 获取影片推荐数据 */
    List<VideoList> queryRecommendVideo(String videoClass, String currVideoId);

    /** 根据id获取数据 */
    VideoList queryVideoByVideoId(String videoId);

    /** 获取播放频率最高的，高清的10条数据 */
    List<VideoList> queryHeightVideo();
}
