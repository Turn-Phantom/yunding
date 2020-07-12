package com.li.cloud.queue.dao;

import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.queue.entity.VisitRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class VisitRecordDaoTest {

    @Autowired
    private VisitRecordDao visitRecordDao;

    @Autowired
    private BaseService baseService;

    @Test
    public void queryRecord(){
        List<VisitRecord> visitRecord = visitRecordDao.queryByField(1, null, "192.168.48.47");
        System.out.println(visitRecord);
    }

    @Test
    public void updateVisitRecord(){
        VisitRecord visitRecord = new VisitRecord();
        visitRecord.setVisitor(1);
        visitRecord.setVisitCount(1);
        visitRecord.setFictitiousCount(1);
        visitRecord.setVisitIp("192.168.48.47");
        visitRecord.setUpdateTime(System.currentTimeMillis());
        visitRecord.setUpdateCount(1);
        visitRecordDao.updateVisitRecord(visitRecord);
    }

    @Test
    public void delRecord(){
        int i = visitRecordDao.deleteByField(1, null, "192.168.48.47");
        System.out.println("删除结果：" + 1);
    }
}