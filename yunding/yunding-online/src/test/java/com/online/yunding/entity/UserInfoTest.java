package com.online.yunding.entity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@SpringBootTest
@RunWith(SpringRunner.class)
public class UserInfoTest {
    private UserInfo userInfo;

    @Before
    public void initUserInfo(){
        userInfo = new UserInfo();
    }

    @Test
    public void testGetter(){
    }

}