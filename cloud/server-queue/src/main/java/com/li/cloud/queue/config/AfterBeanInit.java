package com.li.cloud.queue.config;

import com.li.cloud.queue.service.VideoServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

/**
 * @desc 所有bean初始化完成后执行
 * @date 2020-04-20
 */
@Component
public class AfterBeanInit implements SmartLifecycle {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VideoServerService videoServerService;

    @Override
    public void start() {
        // 将影片播放记录表所有数据加载到redis中
        int loadNum = videoServerService.loadVideoPlayCount();
        logger.info("加载" + loadNum +"条影片播放记录数据到缓存中");
    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
