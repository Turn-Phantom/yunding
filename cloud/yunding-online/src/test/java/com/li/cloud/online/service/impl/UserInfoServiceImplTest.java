package com.li.cloud.online.service.impl;

import com.li.cloud.online.service.UserInfoService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest()
@RunWith(SpringRunner.class)
public class UserInfoServiceImplTest {

    @Autowired
    private UserInfoService userInfoService;

    private UserInfoServiceImpl userInfoServiceImpl;

    @Before
    public void beforeTest(){
        userInfoServiceImpl = new UserInfoServiceImpl(new RestTemplate());
    }

    /** 测试注册云顶用户 */
    @Test
    public void registryYunDingUser(){
        Map<String, String> registerData = new HashMap<>();
        registerData.put("accountNo", "yd7788");
        registerData.put("username", "yd7788");
        registerData.put("password", "231218");
        registerData.put("repassword", "231218");
        registerData.put("realname", "赵明");
        registerData.put("tel", "14541591075");
        registerData.put("qq", "3482105861");
        registerData.put("qkmm", "231218");
        registerData.put("reqkmm", "231218");
        registerData.put("tjid", "");
        registerData.put("submit", " 提 交 ");
        String s = userInfoServiceImpl.reqYunDingReg(registerData);
        System.out.println(s);
    }
}