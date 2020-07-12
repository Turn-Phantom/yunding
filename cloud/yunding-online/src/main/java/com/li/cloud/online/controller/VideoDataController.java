package com.li.cloud.online.controller;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.online.service.VideoDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 视频资源控制器
 * @date 2020-04-17
 */
@RestController
@RequestMapping("/rest/video")
public class VideoDataController {

    @Autowired
    private VideoDataService videoDataService;

    /** 根据视频分类，分页获取视频清单 */
    @GetMapping("/queryVideoListByClass")
    public ReturnData queryVideoListByClass(String cla, Integer pageNo, Integer pageSize){
        if(StringUtils.isBlank(cla) || null == pageNo || null ==pageSize){
            return ReturnData.error("参数不能为空");
        }
        return videoDataService.queryVideoListByClass(cla, pageNo, pageSize);
    }

    /** 根据影片id获取影片数据 */
    @GetMapping("/getVideoByVideoId")
    public ReturnData getVideoById(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("参数不能为空！");
        }
        return videoDataService.getVideoById(videoId);
    }

    /** 获取热播数据 */
    @GetMapping("/queryHotVideo")
    public ReturnData queryHotVideo(){
        return videoDataService.queryHotVideo();
    }

    /** 获取影片推荐数据 */
    @GetMapping("/queryRecommendVideo")
    public ReturnData queryRecommendVideo(String videoClass, String currVideoId){
        if(StringUtils.isBlank(videoClass) || StringUtils.isBlank(currVideoId)){
            return ReturnData.error("参数不能为空");
        }
        return videoDataService.queryRecommendVideo(videoClass, currVideoId);
    }

    /** 获取客户端token */
    @GetMapping("/getVideoToken")
    public ReturnData getVideoToken(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("参数不能为空！");
        }
        return videoDataService.getVideoToken(videoId);
    }

    /** 获取客户端免费token */
    @GetMapping("/queryFredVideoToken")
    public ReturnData queryFredVideoToken(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("参数不能为空！");
        }
        return videoDataService.queryFredVideoToken(videoId);
    }

    /** 记录影片播放 */
    @GetMapping("/recordVideoPlay")
    public void recordVideoPlay(String videoId){
        if(StringUtils.isBlank(videoId)){
            return;
        }
        videoDataService.recordVideoPlay(videoId);
    }

    /** 统计播放时间 */
    @GetMapping("/countPlayTime")
    public void countPlayTime(Integer userId, Integer playTime, Integer countType){
        if(null == userId || null == playTime || countType == null){
            return;
        }
        videoDataService.countPlayTime(userId, playTime, countType);
    }

    /** 获取超清，且播放频率最高的10条视频数据 */
    @GetMapping("/queryHeightVideo")
    public ReturnData queryHeightVideo(){
        return videoDataService.queryHeightVideo();
    }

    /** 获取滚动数据 */
    @GetMapping("/getScrollData")
    public ReturnData getScrollData(){
        return ReturnData.successData(videoDataService.queryScrollData());
    }

}
