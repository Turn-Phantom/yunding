package com.li.cloud.online.service.impl;

import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.utils.InterAddressUtils;
import com.li.cloud.online.config.params.ServiceIdConfig;
import com.li.cloud.online.dao.VisitRecordDao;
import com.li.cloud.online.entity.VisitRecord;
import com.li.cloud.online.service.VisitRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @desc 访问记录实现
 * @date 2020-04-08
 */
@Service
public class VisitRecordServiceImpl implements VisitRecordService {

    public static VisitRecord orgVisitRecord = new VisitRecord();

    @Autowired
    private VisitRecordDao visitRecordDao;

    @Autowired
    private ServiceIdConfig serviceIdConfig;

    @Resource(name = "restTemplateForBalance")
    private RestTemplate restTemplate;

    /** 记录访问信息 */
    @Override
    public String recordVisit(HttpServletRequest request) {
        String userId = request.getHeader("userId");
        // ip
        String ip = InterAddressUtils.getIpForReq(request);
        // 浏览器版本
        String browserVersion = InterAddressUtils.getBrowserVer(request);
        // 操作系统
        String operateSystem = InterAddressUtils.getOperateSysVer(request);
        VisitRecord visitRecord = orgVisitRecord.clone();
        // 根据ip和用户id区分不同的访问者
        if(StringUtils.isEmpty(ip)){
            visitRecord.setVisitor(0);
        } else if(StringUtils.isEmpty(userId)){
            visitRecord.setVisitor(1);
        } else{
            visitRecord.setVisitor(2);
            visitRecord.setVisitId(Integer.parseInt(userId));
        }
        // 根据ip区分是否存在代理ip
        String[] ips = ip.split(",");
        if(ips.length > 1){
            visitRecord.setVisitIp(ips[0]);
            int index = ip.indexOf(",");
            visitRecord.setProxyIp(ip.substring(index + 1));
        } else {
            visitRecord.setVisitIp(ip);
        }
        visitRecord.setVisitCount(1);
        visitRecord.setFictitiousCount(1);
        visitRecord.setBrowserVer(browserVersion);
        visitRecord.setSysVer(operateSystem);
        visitRecord.setVisitSource(request.getHeader("locationHref"));
        visitRecord.setVisitDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        visitRecord.setVisitTime(System.currentTimeMillis());
        visitRecord.setUpdateTime(System.currentTimeMillis());
        visitRecord.setUpdateCount(1);
        try {
            String reqUrl = serviceIdConfig.getQueueUrl() + "/rest/visit/record/addQueue";
            restTemplate.postForObject(reqUrl, visitRecord, ReturnData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ReturnData.SUCCESS;
    }

    /** 分页查询访问记录数据 */
    @Override
    @CustomPage
    public List<VisitRecord> queryVisitDataList(Pagination<VisitRecord> pagination) {
        return visitRecordDao.queryVisitDataList(pagination);
    }

    /**  查询今日/昨日/总浏览量 */
    @Override
    public Map<String, Object> queryViewCountByPart() {
        return visitRecordDao.queryViewCountByPart();
    }
}
