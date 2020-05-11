package com.yunding.server.queue.controller;

import com.yunding.server.common.basecurd.entity.ReturnData;
import com.yunding.server.queue.entity.VisitRecord;
import com.yunding.server.queue.service.VisitRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 访问记录控制器
 * @date 2020-04-08
 */
@RestController
@RequestMapping("/rest/visit/record")
public class VisitRecordController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private VisitRecordService visitRecordService;

    /** 添加访问记录到队列中 */
    @PostMapping("/addQueue")
    public ReturnData recordVisit(@RequestBody VisitRecord visitRecord){
        if(null == visitRecord){
            logger.error("请求参数为空");
            return ReturnData.error("请求参数为空");
        }
        return ReturnData.successData(visitRecordService.recordVisit(visitRecord));
    }
}
