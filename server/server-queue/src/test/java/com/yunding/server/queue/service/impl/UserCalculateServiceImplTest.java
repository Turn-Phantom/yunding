package com.yunding.server.queue.service.impl;

import com.yunding.server.queue.dao.UserCalculateDao;
import com.yunding.server.queue.entity.UserIncome;
import com.yunding.server.queue.service.UserCalculateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserCalculateServiceImplTest {

    @Autowired
    private UserCalculateService userCalculateService;

    // 更新用户访问总量
    @Test
    public void testUpdateUserVisitView(){
        userCalculateService.updateUserVisitView(1, 10);
    }

    // 更新用户在线时长和收益
    @Test
    public void testUpdateUserOnlineTime(){
        userCalculateService.updateUserIncome(1, 3600, new BigDecimal("0.00014").multiply(new BigDecimal("3600")));
    }

    // 根据用户id，查询下级用户id
    @Test
    public void queryChildId(){
        System.out.println(userCalculateService.queryChildId(1));
    }

    @Test
    public void clearUpdateRecordByLast(){
        int clearNum = userCalculateService.clearUserUpdateRecordByLast();
        System.out.println("更新条数为：" + clearNum);
    }

    @Test
    public void querySmsSurplus(){
        userCalculateService.querySmsSurplus();
    }
}