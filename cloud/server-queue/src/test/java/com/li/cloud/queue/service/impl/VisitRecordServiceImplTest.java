package com.li.cloud.queue.service.impl;

import com.li.cloud.queue.service.VisitRecordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class VisitRecordServiceImplTest {

    @Autowired
    private VisitRecordService visitRecordService;

    // 将前一天的访问历史，插入到历史表中
    @Test
    public void updateVisitRecordHis(){
        visitRecordService.insertRecordHisByLastDay();
    }

    // 删除当天访问记录表中，删除前一天的数据
    @Test
    public void delVisitRecordByLastDay(){
        visitRecordService.delVisitRecordByLast();
    }
}