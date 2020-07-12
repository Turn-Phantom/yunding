package com.li.cloud.online.service;

/**
 * @desc 常量数据
 * @date 2020-04-17
 */
public interface ConstantData {
    /** 获取视频类别清单数据 */
    String VIDEO_LIST_FOR_CLASS = "/rest/video/list/queryPageByClass?cla=%s&pageNo=%d&pageSize=%d";

    /** 根据id获取影片数据 */
    String GET_VIDEO_BY_VIDEO_ID = "/rest/video/list/getVideoByVideoId?videoId=%s";

    /** 获取影片热播数据 */
    String GET_HOT_VIDEO = "/rest/video/list/getHotVideo";

    /** 获取影片token */
    String GET_VIDEO_TOKEN = "/rest/video/list/getVideoToken?videoId=%s";

    /** 获取免费影片的token */
    String GET_FREE_VIDEO_TOKEN = "/rest/video/list/getFreeVideoToken?videoId=%s";

    /** 获取推荐影片数据 */
    String QUERY_RCMD_VIDEO = "/rest/video/list/queryRecommendVideo?videoClass=%s&currVideoId=%s";

    /** 队列：记录影片播放次数 */
    String RECORD_VIDEO_PLAY = "/rest/video/server/recordVideoPlay?videoId=%s";

    /** 统计播放时长 */
    String COUNT_PLAY_TIME = "/rest/video/server/countPlayTime?userId=%d&playTime=%d&countType=%d";

    /** 获取播放频率最高的，高清的10条数据 */
    String QUERY_HEIGHT_VIDEO = "/rest/video/list/queryHeightVideo";
}
