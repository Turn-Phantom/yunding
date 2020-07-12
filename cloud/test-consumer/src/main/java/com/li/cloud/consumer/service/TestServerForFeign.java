package com.li.cloud.consumer.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @desc
 * @date 2020-05-18
 */
@FeignClient(name = "server-tool", fallback = TestServiceFallBack.class) // 指定服务名，绑定服务请求
public interface TestServerForFeign {

    @RequestMapping("/cloud/server/tool/rest/test") // 直接绑定映射接口
    String testVisitServerTool();
}
