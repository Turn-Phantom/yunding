package com.li.cloud.consumer.controller;

import com.li.cloud.consumer.service.TestServerForFeign;
import com.li.cloud.consumer.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @desc 测试消费服务 控制器
 * @date 2020-05-13
 */
@RestController
@RequestMapping("/rest/consumer")
public class TestConsumerController {

    @Autowired
    private TestService testServcie;

    @Autowired
    private TestServerForFeign testServerForFeign;

    /** 测试请求tool-service 服务，测试负载均衡 */
    @RequestMapping("/restReqToolServer")
    public String restReqToolServer(){
        return testServcie.testVisitServerTool();
    }

    @RequestMapping("/testForFeign")
    public String testForFeign(){
        return testServerForFeign.testVisitServerTool();
    }
}
