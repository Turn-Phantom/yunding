package com.yunding.server.queue.service.impl;

import com.yunding.server.common.basecurd.service.BaseService;
import com.yunding.server.common.utils.DateUtil;
import com.yunding.server.queue.constant.ConstantStr;
import com.yunding.server.queue.dao.VideoServerDao;
import com.yunding.server.queue.entity.LinkedQueue;
import com.yunding.server.queue.entity.UserIncome;
import com.yunding.server.queue.entity.UserOnlineTime;
import com.yunding.server.queue.entity.VideoPlayCount;
import com.yunding.server.queue.service.UserCalculateService;
import com.yunding.server.queue.service.VideoServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc 视频服务 逻辑实现
 * @date 2020-04-20
 */
@Service
public class VideoServerServiceImpl implements VideoServerService {

    @Autowired
    private BaseService baseService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private VideoServerDao videoServerDao;

    @Autowired
    private UserCalculateService userCalculateService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 更新影片播放次数 */
    @Override
    public int updateVideoPlayCount() {
        // 判断key是否存在缓存中
        Map<String, Integer> videoPlayCountMap = (Map<String, Integer>) redisTemplate.opsForValue().get(VIDEO_ID_LIST_KEY);
        Queue<String> videoPlayRecord = LinkedQueue.videoPlayRecord;
        while (videoPlayRecord.size() > 0){
            String videoId = videoPlayRecord.poll();
            VideoPlayCount videoPlayCount = new VideoPlayCount();
            videoPlayCount.setVideoId(videoId);
            // 不存在key，插入; 否则更新
            if(!videoPlayCountMap.containsKey(videoId)){
                // 插入数据库
                videoPlayCount.setClickPlay(1);
                videoPlayCountMap.put(videoId, 1);
                redisTemplate.opsForValue().set(VIDEO_ID_LIST_KEY, videoPlayCountMap);
                baseService.insert(videoPlayCount);
            } else{
                // 根据影片id更新
                int count = videoPlayCountMap.get(videoId)+1;
                videoServerDao.updateVideoPlayCount(videoId, count);
                videoPlayCountMap.put(videoId, count);
                redisTemplate.opsForValue().set(VIDEO_ID_LIST_KEY, videoPlayCountMap);
            }
        }
        return 1;
    }

    /** 装载所有影片播放次数到redis中 */
    @Override
    public int loadVideoPlayCount() {
        List<VideoPlayCount> videoPlayCounts = videoServerDao.queryAllPlayCount();
        if(null == videoPlayCounts){
            videoPlayCounts = new ArrayList<>();
        }
        Map<String, Integer> mapData = videoPlayCounts.stream().collect(Collectors.toMap(VideoPlayCount::getVideoId, VideoPlayCount::getClickPlay));
        redisTemplate.opsForValue().set(ConstantStr.VIDEO_ID_LIST_KEY, mapData);
        // key永久保存
        redisTemplate.persist(VIDEO_ID_LIST_KEY);
        return videoPlayCounts.size();
    }

    /** 保存用户时长 */
    @Override
    public void updatePlayTimeCount() {
        Queue<UserOnlineTime> videoPlayTimeCount = LinkedQueue.videoPlayTimeCount;
        String currDate = DateUtil.getCurrDate("yyyyMMdd");
        while (videoPlayTimeCount.size() > 0){
            // 取出队列中的对象
            UserOnlineTime onlineTime = videoPlayTimeCount.poll();
            assert onlineTime != null;
            onlineTime.setEndTime(System.currentTimeMillis());
            Integer userId = onlineTime.getUserId();
            // 查询数据库
            UserOnlineTime currOnlineData = videoServerDao.queryCurrOnlineUser(userId);
            if(null == currOnlineData){
                // 插入数据库
                onlineTime.setOnlineDate(currDate);
                onlineTime.setStartTime(System.currentTimeMillis());
                baseService.insertNoId(onlineTime);
            } else{
                // 更新数据库
                videoServerDao.updateVideoPlayTime(userId, currOnlineData.getOnlineSec() + onlineTime.getOnlineSec());
            }
        }
    }

    /** 清除播记录超过15天的数据 */
    @Override
    public int clearPlayTimeHis() {
        return videoServerDao.clearPlayTimeHis();
    }

    /** 将前一天的数据，更新到历史播放时长记录表中 */
    @Override
    public int insertPlayHis() {
        try {
            // 查询前一天用户播放时长记录
            List<UserOnlineTime> userOnlineTimeList = queryPlayTimeByLastDay();
            userOnlineTimeList.forEach(userOnlineTime -> {
                int userId = userOnlineTime.getUserId();
                long onlineSecond = (long)userOnlineTime.getOnlineSec();
                // 根据id查询用户收益数据
                UserIncome userIncome = userCalculateService.queryIncomeByUserId(userId);
                // 根据id查询该用户的下级代理用户id
                List<Integer> userIds = userCalculateService.queryChildId(userId);
                // 统计当前用户的下级代理时长
                BigDecimal childIncome = getChildIncomeTotal(userIds, userOnlineTimeList);
                if(null == userIncome){
                    userIncome = new UserIncome();
                    userIncome.setUserId(userId);
                    userIncome.setOnlineTimeTotal(onlineSecond);
                    // 每小时0.504元收益； 每秒收益：0.00014； 乘以时长 得出总收益
                    userIncome.setIncomeTotal(new BigDecimal("0.00014").multiply(new BigDecimal(String.valueOf(onlineSecond))).add(childIncome));
                    baseService.insertNoId(userIncome);
                } else{
                    // 更新用户在线时长和收益
                    long onlineSec = onlineSecond + (null == userIncome.getOnlineTimeTotal()? 0 : userIncome.getOnlineTimeTotal());
                    BigDecimal incomeTotal = null == userIncome.getIncomeTotal()? BigDecimal.ZERO: userIncome.getIncomeTotal();
                    BigDecimal incomeTol = new BigDecimal("0.00014").multiply(new BigDecimal(String.valueOf(onlineSecond))).add(incomeTotal).add(childIncome);
                    userCalculateService.updateUserIncome(userId, onlineSec, incomeTol);
                }
            });
            return videoServerDao.insertPlayHis();
        } catch (Exception e) {
            logger.error("用户播放时长记录历史表更新数失败", e);
            return -10;
        }
    }

    /** 获取下级用户的收益 */
    private BigDecimal getChildIncomeTotal(List<Integer> userIds, List<UserOnlineTime> userOnlineTimeList) {
        BigDecimal childIncome = BigDecimal.ZERO;
        for (UserOnlineTime userOnlineTime : userOnlineTimeList) {
            // 若不属于下级用户，则跳过
            if(!userIds.contains(userOnlineTime.getUserId())){
               continue;
            }
             // 累计下级用户的百分之10的收益
            long onlineSecond = (long)userOnlineTime.getOnlineSec();
            BigDecimal incomeTol = new BigDecimal("0.00014").multiply(new BigDecimal(String.valueOf(onlineSecond)));
            childIncome = childIncome.add(incomeTol.multiply(new BigDecimal("0.1")));
        }
        return childIncome;
    }

    private List<UserOnlineTime> queryPlayTimeByLastDay() {
        return videoServerDao.queryPlayTimeByLastDay();
    }

    /** 在用户播放时长记录表中，删除前一天的数据 */
    @Override
    public int delPlayByLast() {
        return videoServerDao.delPlayByLast();
    }
}
