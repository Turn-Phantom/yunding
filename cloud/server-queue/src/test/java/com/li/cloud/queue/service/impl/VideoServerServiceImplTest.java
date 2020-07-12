package com.li.cloud.queue.service.impl;

import com.li.cloud.queue.service.VideoServerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VideoServerServiceImplTest {

    @Autowired
    private VideoServerService videoServerService;

    // 测试将前一天的历史数据，插入到历史表
    @Test
    public void testInsertPlayHis(){
        int i = videoServerService.insertPlayHis();
        System.out.println("插入结果：" + i);
    }

    // 删除前一天的数据，
    @Test
    public void testDelPlayDataForLastDay(){
        int i = videoServerService.delPlayByLast();
        System.out.println("删除前一天的结果数：" + i);
    }
}