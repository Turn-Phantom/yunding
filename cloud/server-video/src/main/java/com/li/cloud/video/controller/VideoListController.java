package com.li.cloud.video.controller;

import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.video.entity.VideoClassExchage;
import com.li.cloud.video.entity.VideoList;
import com.li.cloud.video.service.VideoListService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 视频清单 控制器
 * @date 2020-04-17
 */
@RestController
@RequestMapping("/rest/video/list")
public class VideoListController {

    @Autowired
    private VideoListService videoListService;

    @Autowired
    private BaseService baseService;

    /** 根据视频分类，分页获取视频清单 */
    @GetMapping("/queryPageByClass")
    public ReturnData queryPageByClass(String cla, Integer pageNo, Integer pageSize){
        Pagination<VideoList> pagination = new Pagination<>();
        if(StringUtils.isBlank(cla) || null == pageNo || null ==pageSize){
            return ReturnData.error("参数不能为空");
        }
        String videoClass = VideoClassExchage.exchangeData().get(cla);
        if("hotVideo".equals(cla)){
            pagination.getParams().put("specialVideo", "hotVideo");
        } else if(StringUtils.isEmpty(videoClass)){
            return ReturnData.error("未找到对应的类别: " + cla);
        }
        pagination.setPageNo(pageNo);
        pagination.setPageSize(pageSize);
        pagination.getParams().put("videoClass", videoClass);
        if("mianfei".equals(cla)){
            pagination.getParams().put("specialVideo", "mianfei");
        } else{
            pagination.getParams().put("specialVideo", "login");
        }
        videoListService.queryPageByClass(pagination);
        return ReturnData.successData(pagination);
    }

    /** 获取热播视频 */
    @GetMapping("/getHotVideo")
    public ReturnData queryHotVideo(){
        return ReturnData.successData(videoListService.queryHotVideo());
    }

    /** 根据id获取数据 */
    @GetMapping("/getVideoByVideoId")
    public ReturnData queryVideoByVideoId(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("获取失败：影片id为空");
        }
        return ReturnData.successData(videoListService.queryVideoByVideoId(videoId));
    }

    /** 获取客户端token */
    @GetMapping("/getVideoToken")
    public ReturnData getVideoToken(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("视频资源服务：参数不能为空！");
        }
        return ReturnData.successData(videoListService.queryVideoToken("", videoId));
    }

    /** 获取免费影片token */
    @GetMapping("/getFreeVideoToken")
    public ReturnData getFreeVideoToken(String videoId){
        if(StringUtils.isBlank(videoId)){
            return ReturnData.error("视频资源服务：参数不能为空！");
        }
        return ReturnData.successData(videoListService.queryVideoToken("free", videoId));
    }

    /** 获取影片推荐数据 */
    @GetMapping("/queryRecommendVideo")
    public ReturnData queryRecommendVideo(String videoClass, String currVideoId){
        if(StringUtils.isBlank(videoClass) || StringUtils.isBlank(currVideoId)){
            return ReturnData.error("参数不能为空： 视频资源管理服务");
        }
        return ReturnData.successData(videoListService.queryRecommendVideo(videoClass, currVideoId));
    }

    /** 获取播放频率最高的，高清的10条数据 */
    @GetMapping("/queryHeightVideo")
    public ReturnData queryHeightVideo(){
        return ReturnData.successData(videoListService.queryHeightVideo());
    }
}
