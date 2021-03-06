package com.yunding.server.video.service.impl;

import com.yunding.server.common.utils.DateUtil;
import com.yunding.server.video.service.VideoScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VideoScheduleServiceImplTest {

    @Autowired
    private VideoScheduleService videoScheduleService;

    @Test
    public void checkModifyVideo(){
        videoScheduleService.checkModifyVideo(DateUtil.getUTCDate("yyyyMMdd", -4L));
    }

    @Test
    public void updateVideoData(){
        videoScheduleService.updateCurrVideo(DateUtil.getUTCDate("yyyyMMdd", -4L) );
    }
}