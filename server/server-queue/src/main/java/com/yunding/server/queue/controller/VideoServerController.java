package com.yunding.server.queue.controller;

import com.yunding.server.queue.entity.LinkedQueue;
import com.yunding.server.queue.entity.UserOnlineTime;
import com.yunding.server.queue.utils.OriginalObjUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @desc 接受视频服务请求 控制器
 * @date 2020-04-20
 */
@RestController
@RequestMapping("/rest/video/server")
public class VideoServerController {

    /** 记录视频播放次数 */
    @GetMapping("/recordVideoPlay")
    public void recordVideoPlay(String videoId){
        LinkedQueue.videoPlayRecord.offer(videoId);
    }

    /** 统计用户播放影片时长 */
    @GetMapping("/countPlayTime")
    public void countPlayTime(Integer userId, Integer playTime, Integer countType){
        if(null == userId || null == playTime || countType == null){
            return;
        }
        UserOnlineTime userOnlineTime = OriginalObjUtil.orgOnlineTime.clone();
        userOnlineTime.setUserId(userId);
        userOnlineTime.setOnlineSec(playTime);
        LinkedQueue.videoPlayTimeCount.offer(userOnlineTime);
    }
}
