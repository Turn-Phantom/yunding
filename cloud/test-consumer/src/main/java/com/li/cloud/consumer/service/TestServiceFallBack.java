package com.li.cloud.consumer.service;

import org.springframework.stereotype.Component;

/**
 * @desc 服务降级实现类
 * @date 2020-05-18
 */
@Component
public class TestServiceFallBack implements TestServerForFeign{

    @Override
    public String testVisitServerTool() {
        return "error message is fallback";
    }
}
