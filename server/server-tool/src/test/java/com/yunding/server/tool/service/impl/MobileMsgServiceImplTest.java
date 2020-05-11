package com.yunding.server.tool.service.impl;

import com.yunding.server.tool.entity.MobileNumInfo;
import com.yunding.server.tool.service.MobileMsgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileMsgServiceImplTest {

    @Autowired
    private MobileMsgService mobileMsgService;

    @Test
    public void batchInsert(){
        List<MobileNumInfo> mobileNumInfos = new ArrayList<>();
        MobileNumInfo mobileNumInfo = new MobileNumInfo();
        mobileNumInfo.setPhoneNum("15917109839");
        MobileNumInfo mobileNumInfo1 = new MobileNumInfo();
        mobileNumInfo1.setPhoneNum("15917109839");
        MobileNumInfo mobileNumInfo2 = new MobileNumInfo();
        mobileNumInfo2.setPhoneNum("15917109839");
        mobileNumInfos.add(mobileNumInfo);
        mobileNumInfos.add(mobileNumInfo1);
        mobileNumInfos.add(mobileNumInfo2);
        mobileMsgService.importPhoneNum(mobileNumInfos);
    }

}