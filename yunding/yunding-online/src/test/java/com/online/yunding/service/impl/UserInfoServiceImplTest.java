package com.online.yunding.service.impl;

import com.online.yunding.common.basecurd.service.BaseService;
import com.online.yunding.entity.UserInfo;
import com.online.yunding.entity.VisitRecord;
import com.online.yunding.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserInfoServiceImplTest {

    @Autowired
    private BaseService baseService;

    private UserInfo userInfo;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Before
    public void initUserInfo(){
        userInfo = new UserInfo();
    }

    @Test
    public void TestCreateTime(){
        userInfo.setPhoneNo("15917109839");
        UserInfo userInfo = baseService.queryDataByField(this.userInfo, "phoneNo");
        System.out.println(userInfo);
    }

    @Test
    public void testRedis(){

        System.out.println(redisTemplate.opsForValue().get("192.168.48.47:1586572239"));
    }

    @Test
    public void addRecommendMoney(){
        userInfoService.addBalance(3);
    }
}