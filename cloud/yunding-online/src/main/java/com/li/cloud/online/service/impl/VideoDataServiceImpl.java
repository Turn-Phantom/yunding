package com.li.cloud.online.service.impl;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.online.config.params.ConfigParams;
import com.li.cloud.online.config.params.ServiceIdConfig;
import com.li.cloud.online.dao.VideoDataDao;
import com.li.cloud.online.service.VideoDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @desc 视频清单数据 业务
 * @date 2020-04-17
 */
@Service
public class VideoDataServiceImpl implements VideoDataService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VideoDataDao videoDataDao;

    @Resource(name = "restTemplateForBalance")
    private RestTemplate restTemplate;

    @Autowired
    private ServiceIdConfig serviceIdConfig;

    /** 根据类别获取视频清单数据 */
    @Override
    public ReturnData queryVideoListByClass(String cla, Integer pageNo, Integer pageSize) {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + String.format(this.VIDEO_LIST_FOR_CLASS, cla, pageNo, pageSize), ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 根据id获取影片数据 */
    @Override
    public ReturnData getVideoById(String videoId) {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + String.format(this.GET_VIDEO_BY_VIDEO_ID, videoId), ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 获取热播数据 */
    @Override
    public ReturnData queryHotVideo() {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + this.GET_HOT_VIDEO, ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 获取影片推荐数据 */
    @Override
    public ReturnData queryRecommendVideo(String videoClass, String currVideoId) {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + String.format(this.QUERY_RCMD_VIDEO, videoClass, currVideoId), ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 获取影片token; */
    @Override
    public ReturnData getVideoToken(String videoId) {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + String.format(this.GET_VIDEO_TOKEN, videoId), ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 获取客户端免费token */
    @Override
    @SuppressWarnings("unchecked")
    public ReturnData queryFredVideoToken(String videoId) {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + String.format(this.GET_FREE_VIDEO_TOKEN, videoId), ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        if(returnData.getReturnType().toString().equals(ReturnData.SUCCESS)){
            Map<String, Object> objectData = (Map<String, Object>) returnData.getObjectData();
            List<String> videoIdList = (List<String>) objectData.get("freeVideoCid");
            if(!videoIdList.contains(videoId)){
                return ReturnData.error("请登录后播放影片！");
            }
        }
        return returnData;
    }

    /** 记录影片播放 */
    @Override
    public void recordVideoPlay(String videoId) {
        restTemplate.getForObject(serviceIdConfig.getQueueUrl() + String.format(this.RECORD_VIDEO_PLAY, videoId), ReturnData.class);
    }

    /** 统计用户播放时长 */
    @Override
    public void countPlayTime(Integer userId, Integer playTime, Integer countType) {
        restTemplate.getForObject(serviceIdConfig.getQueueUrl() + String.format(this.COUNT_PLAY_TIME, userId, playTime, countType), ReturnData.class);
    }

    /** 获取超清，且播放频率最高的10条视频数据 */
    @Override
    public ReturnData queryHeightVideo() {
        ReturnData returnData = restTemplate.getForObject(serviceIdConfig.getVideoUrl() + this.QUERY_HEIGHT_VIDEO, ReturnData.class);
        if(null == returnData){
            logger.error("未知错误：请求接口返回null，请查看视频服务日志");
            return ReturnData.error("未知错误：请求接口返回null，请查看视频服务日志");
        }
        return returnData;
    }

    /** 获取滚动数据 */
    @Override
    public List<Map<String, Object>> queryScrollData() {
        return videoDataDao.queryScrollData();
    }
}
