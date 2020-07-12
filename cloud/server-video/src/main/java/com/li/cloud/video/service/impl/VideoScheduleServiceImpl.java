package com.li.cloud.video.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.video.config.YunDingParams;
import com.li.cloud.video.entity.VideoList;
import com.li.cloud.video.service.VideoListService;
import com.li.cloud.video.service.VideoScheduleService;
import com.li.cloud.video.utils.OriginalObjUtil;
import com.li.cloud.video.utils.VideoServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @desc 视频调度 逻辑
 * @date 2020-04-18
 */
@Service
public class VideoScheduleServiceImpl implements VideoScheduleService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private YunDingParams yunDingParams;

    @Autowired
    private VideoListService videoListService;

    @Autowired
    private BaseService baseService;

    /** 检查异动数据 */
    @Override
    public void checkModifyVideo(String date) {
        try {
            String reqUrl = String.format(yunDingParams.getVideoConfig().getVideoChangeUrl(), date);
            JSONObject jsonObject = restTemplate.getForObject(reqUrl, JSONObject.class);
            String checkRes = checkRepData(jsonObject, "影片异动数据检查结果：");
            if(!checkRes.equals(ReturnData.SUCCESS)){
                videoListService.insertOperateLog("影片异动检查", DateUtil.format(new Date(), "yyyy年MM月dd日") + "：检查并更新影片数据", checkRes);
                return;
            }
            logger.info("开始处理影片异动数据  ===================》");
            // 将结果数据与数据库数据进行对比
            handleData(jsonObject, "影片异动数据操作结果：");
        } catch (RestClientException e) {
            videoListService.insertOperateLog("影片异动检查", DateUtil.format(new Date(), "yyyy年MM月dd日") + "：检查并更新影片数据", "检查失败：" + e.getMessage());
            logger.error("处理影片异动数据失败", e);
        }
    }

    /** 将结果数据与数据库数据进行对比 */
    private void handleData(JSONObject jsonObject, String msg) {
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        int size = jsonArray.size();
        logger.info("待处理数据：" + size + "条");
        Map<String, VideoList> videoListMap = new HashMap<>();
        Set<String> videoIds = new HashSet<>();
        for (int i = 0; i < size; i++) {
            JSONObject videoJsonData = jsonArray.getJSONObject(i);
            VideoList videoList = OriginalObjUtil.orgVideoList.clone();
            // 将json对象的数据结果转换为视频清单对象
            VideoServerUtil.setVideoListVal(videoList, videoJsonData);
            String cid = videoJsonData.getString("cid");
            videoIds.add(cid);
            videoListMap.put(cid, videoList);
        }
        /* 对比数据库异动数据，存在则对比数据，有差异则更新；若不存在则插入数据 */
        int insertNum = 0;
        int updateNum = 0;
        // 根据数据的key，查询数据库中的数据
        List<VideoList> videoListDatas = videoListService.queryVideoList(videoIds);
        assert null != videoListDatas;
        for (VideoList videoListData : videoListDatas) {
            String cid = videoListData.getCid();
            VideoList dbVideoList = videoListMap.get(cid);
            if(null == dbVideoList){
                insertNum += baseService.insert(videoListData);
            } else{
                // 对比数据与异动的数据; 排除对比字段
                boolean compareRes = VideoServerUtil.compareData(dbVideoList, videoListData,
                        new String[]{"id","updateTime","clickCount","enableDate","operateTime"});
                if(compareRes){
                    updateNum += baseService.updateField(dbVideoList);
                }
            }
            // 操作完成后，移除查询到的结果数据
            videoListMap.remove(cid);
        }
        // 若对比完成后，仍然存在数据，则继续执行插入
        if(!videoListMap.isEmpty()){
            for (VideoList videoList : videoListMap.values()) {
                insertNum += baseService.insert(videoList);
            }
        }
        String message = "新增数据：" + insertNum + "；更新数据：" + updateNum;
        String content = DateUtil.format(new Date(), "yyyy年MM月dd日") +":"+ "待处理数据：" + size + "条";
        videoListService.insertOperateLog(msg.contains("异动")?"影片异动检查":"影片数据更新", content, message);
        logger.info(msg + message);
    }

    /** 更新当天数据 */
    @Override
    public void updateCurrVideo(String date) {
        try {
            String reqUrl = String.format(yunDingParams.getVideoConfig().getVideoUpdateUrl(), date);
            JSONObject jsonObject = restTemplate.getForObject(reqUrl, JSONObject.class);
            String checkRes = checkRepData(jsonObject, "影片更新数据检查结果：");
            if(!ReturnData.SUCCESS.equals(checkRes)){
                videoListService.insertOperateLog("新增影片数据", DateUtil.format(new Date(), "yyyy年MM月dd日") + "：新增影片数据", checkRes);
                return;
            }
            logger.info("开始处理影片更新数据  ===================》");
            // 将结果数据与数据库数据进行对比
            handleData( jsonObject, "影片更新数据操作结果：");
        } catch (RestClientException e) {
            videoListService.insertOperateLog("新增影片数据", DateUtil.format(new Date(), "yyyy年MM月dd日") + "：新增影片数据", "更新失败：" + e.getMessage());
            logger.error("处理影片更新数据失败：", e);
        }
    }

    /** 检查请求返回数据 */
    private String checkRepData(JSONObject jsonObject, String errMsg){
        if(null == jsonObject){
            logger.error(errMsg + "请求结果返回 null");
            return errMsg + "请求结果返回 null";
        }
        Integer statusCode = jsonObject.getInteger("code");
        if(300 == statusCode){
            logger.info(errMsg + "暂无资料数据");
            return errMsg + "暂无资料数据";
        }
        if(200 != statusCode){
            logger.info(errMsg + "未知错误；" + jsonObject.toJSONString());
            return errMsg + "未知错误";
        }
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        if(null == jsonArray || jsonArray.isEmpty()){
            logger.info(errMsg + "data数据为空");
            return errMsg + "data数据为空";
        }
        return ReturnData.SUCCESS;
    }
}
