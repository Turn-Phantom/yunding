package com.li.cloud.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @desc
 * @date 2020-05-18
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private RestTemplate restTemplate;

    /** 测试访问 server-tool 服务接口 */
    @Override
    public String testVisitServerTool(){
        System.out.println("测试消费服务，请求server-too服务：");
        ResponseEntity<String> forEntity = restTemplate.getForEntity("http://tool-service/cloud/server/tool/rest/test", String.class);
        return forEntity.getBody();
    }

    public String callBackContent(){
        return "other callback content";
    }
}
