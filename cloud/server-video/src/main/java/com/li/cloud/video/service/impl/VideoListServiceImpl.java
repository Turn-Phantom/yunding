package com.li.cloud.video.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.DateUtil;
import com.li.cloud.video.config.VideoConfig;
import com.li.cloud.video.config.YunDingParams;
import com.li.cloud.video.dao.VideoListDao;
import com.li.cloud.video.entity.VideoClassExchage;
import com.li.cloud.video.entity.VideoList;
import com.li.cloud.video.entity.VideoOperateLog;
import com.li.cloud.video.service.VideoListService;
import com.li.cloud.video.utils.OriginalObjUtil;
import com.li.cloud.video.utils.VideoServerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @desc 影片清单 业务实现
 * @date 2020-04-16
 */
@Service
public class VideoListServiceImpl implements VideoListService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private YunDingParams yunDingParams;

    @Autowired
    private BaseService baseService;

    @Autowired
    private VideoListDao videoListDao;

    /** 从第三方汇入影片清单; 每小时只能获取一次，一个小时候则获取失败 */
    @Override
    public int insertVideoList() {
        JSONObject videoListJson = restTemplate.getForObject(yunDingParams.getVideoConfig().getVideoListUrl(), JSONObject.class);
        if(null == videoListJson || videoListJson.getInteger("code") != 200){
            this.insertOperateLog("汇入影片清单", "请求第三方资源（影片清单）数据",
                    videoListJson!=null?String.format("状态码：%d, 结果描述：%s", videoListJson.getInteger("code"), videoListJson.getString("desc")):"返回数据为空");
            logger.error("获取影片清单失败！");
            return 0;
        }
        List<List<VideoList>> batchList = new ArrayList<>();
        List<VideoList> videoListList = new ArrayList<>();
        JSONArray jsonArray = videoListJson.getJSONArray("data");
        int size = jsonArray.size();
        int count = 0;
        for (int i = 0; i <size; i++) {
            if(count >= 800){
                batchList.add(videoListList);
                videoListList = new ArrayList<>();
                count = 0;
            }
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            VideoList videoList = OriginalObjUtil.orgVideoList.clone();
            // 将json对象的数据结果转换为视频清单对象
            VideoServerUtil.setVideoListVal(videoList, jsonObject);
            videoListList.add(videoList);
            count ++;
        }
        // 增加最后一次集合
        if(count > 0){
            batchList.add(videoListList);
        }

        return insertDb(batchList);
    }

    /** 插入操作 */
    private int insertDb(List<List<VideoList>> batchList){
        int insertNum = 0;
        for (List<VideoList> videoLists : batchList) {
            insertNum += videoListDao.insertVideoList(videoLists);
        }
        if(insertNum <= 0){
            return 0;
        }
        // 记录操作日志
        this.insertOperateLog("汇入影片清单", "请求第三方资源（影片清单）数据", "汇入结果数：" + insertNum);
        return insertNum;
    }

    /**
     * 记录操作日志
     * @param pos 操作位置
     * @param content 操作内容
     * @param res 操作结果
     */
    @Override
    public void insertOperateLog(String pos, String content, String res){
        VideoOperateLog operateLog = new VideoOperateLog();
        operateLog.setOperatePos(pos);
        operateLog.setOperateContent(content);
        operateLog.setOperateRes(res);
        operateLog.setOperateTime(System.currentTimeMillis());
        int insertNum = baseService.insert(operateLog);
        if(insertNum <= 0){
            logger.error("保存影片操作日志失败：" + content);
        }
    }

    /** 根据视频分类，分页获取视频清单 */
    @Override
    @CustomPage // 作用：before：设置分页条件，afterReturn：设置分页数据
    public List<VideoList> queryPageByClass(Pagination<VideoList> pagination) {
        return videoListDao.queryPageByClass(pagination);
    }

    /** 根据id获取影片数据 */
    @Override
    public VideoList queryVideoByVideoId(String videoId) {
        return videoListDao.queryVideoByVideoId(videoId);
    }

    /** 根据视频id获取视频清单 */
    @Override
    public List<VideoList> queryVideoList(Set<String> videoId) {
        return videoListDao.queryVideoList(videoId);
    }

    /** 获取热播数据 */
    @Override
    public List<VideoList> queryHotVideo() {
        return videoListDao.queryHotVideo();
    }

    /** 获取影片token */
    @Override
    public Map<String, Object> queryVideoToken(String flag, String videoId) {
        Map<String, Object> retMap = new HashMap<>();
        // 若为免费影片，则查询标识为免费，且画质为300k的数据
        if("free".equals(flag)){
            List<String> freeVideoCid = videoListDao.queryVideoListByFree();
            retMap.put("freeVideoCid", freeVideoCid);
        }
        // 获取utc+0的时间，单位：秒
        long time = DateUtil.getUTCTime();
        VideoConfig videoConfig = yunDingParams.getVideoConfig();
        String token = DigestUtil.md5Hex(String.format("merchant:%s:%s:%d", videoConfig.getSid(), videoConfig.getSecret(), time));
        retMap.put("sid", videoConfig.getSid());
        retMap.put("time", time);
        retMap.put("token", token);
        retMap.put("videoDns", videoConfig.getVideoDns());
        return retMap;
    }

    /** 获取影片推荐数据 */
    @Override
    public List<VideoList> queryRecommendVideo(String videoClass, String currVideoId) {
        String exchange = VideoClassExchage.exchangeData().get(videoClass);
        return videoListDao.queryRecommendVideo(videoClass, currVideoId, exchange);
    }

    /** 获取播放频率最高的，高清的10条数据 */
    @Override
    public List<VideoList> queryHeightVideo() {
        return videoListDao.queryHeightVideo();
    }
}
