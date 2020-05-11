package com.online.yunding.controller;

import com.online.yunding.common.basecurd.entity.Pagination;
import com.online.yunding.common.basecurd.entity.ReturnData;
import com.online.yunding.entity.VisitRecord;
import com.online.yunding.service.VisitRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @desc 访问记录 控制器
 * @date 2020-04-07
 */
@RestController
@RequestMapping("/rest/visit")
public class VisitRecordController {

    @Autowired
    private VisitRecordService visitRecordService;

    /** 分页查询访问记录数据 */
    @GetMapping("/queryVisitData")
    public ReturnData queryVisitData(Pagination<VisitRecord> pagination){
        visitRecordService.queryVisitDataList(pagination);
        return ReturnData.successData(pagination);
    }

    /** 记录访问者信息 */
    @GetMapping("/refreshTime")
    public ReturnData recordVisit(HttpServletRequest request){
        visitRecordService.recordVisit(request);
        return ReturnData.success("");
    }

    /**  查询今日/昨日/总浏览量 */
    @GetMapping("/queryViewCountByPart")
    public ReturnData queryViewCountByPart(){
        return ReturnData.successData(visitRecordService.queryViewCountByPart());
    }

}
