package com.yunding.server.video.dao;

import com.yunding.server.common.basecurd.entity.Pagination;
import com.yunding.server.video.entity.VideoList;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @desc 影片清单
 * @date 2020-04-17
 */
public interface VideoListDao {

    /** 汇入影片清单 */
    int insertVideoList(@Param("videoListList") List<VideoList> videoListList);

    /** 根据类别获取分页视频清单数据 */
    List<VideoList> queryPageByClass(Pagination<VideoList> pagination);

    /** 根据视频id获取视频清单 */
    List<VideoList> queryVideoList(@Param("videoIds") Set<String> videoIds);

    /** 查询免费影片的cid; 画质为300k的数据 */
    List<String> queryVideoListByFree();

    /** 获取热播数据 */
    List<VideoList> queryHotVideo();

    /** 获取影片推荐数据 */
    List<VideoList> queryRecommendVideo(@Param("videoClass") String videoClass, @Param("currVideoId") String currVideoId, @Param("exchangeCla") String exchangeCla);

    /** 根据id获取影片数据 */
    VideoList queryVideoByVideoId(String videoId);

    /** 获取播放频率最高的，高清的10条数据 */
    List<VideoList> queryHeightVideo();
}
