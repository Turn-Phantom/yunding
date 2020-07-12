package com.li.cloud.tool.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.li.cloud.common.annotations.CustomPage;
import com.li.cloud.common.basecurd.entity.Pagination;
import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.common.basecurd.service.BaseService;
import com.li.cloud.common.utils.NumberUtils;
import com.li.cloud.tool.config.PropertiesParam;
import com.li.cloud.tool.dao.CustomerManageDao;
import com.li.cloud.tool.entity.CustomerExportData;
import com.li.cloud.tool.entity.CustomerManage;
import com.li.cloud.tool.service.CustomerManageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @desc 客户资源管理
 * @date 2020-06-01
 */
@Service
public class CustomerManageServiceImpl implements CustomerManageService {

    // 日志
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 空号检测状态（若后台子线程在检查中，则该字段为true，否则为false） */
    public static boolean emptyCheckStatus = false;
    /** 空号号码 */
    private static List<String> emptyNumberList = new LinkedList<>();

    @Autowired
    private CustomerManageDao customerManageDao;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PropertiesParam propertiesParam;

    /** 分页查询数据 */
    @Override
    @CustomPage
    public List<CustomerManage> queryPageList(Pagination<CustomerManage> pagination) {
        String duplicate = (String) pagination.getParams().get("duplicate");
        if(StringUtils.isNotBlank(duplicate) && StringUtils.equals(duplicate, "duplicateNumber")){
            return customerManageDao.queryPageListForDup(pagination);
        }
        return customerManageDao.queryPageList(pagination);
    }

    /** 插入数据 */
    @Override
    public int importPhoneNum(List<CustomerManage> insertList) {
        return customerManageDao.importPhoneNum(insertList);
    }

    /** 导出数据 */
    @Override
    public List<CustomerExportData> exportCustomerData(Map<String, Object> params) {
        return customerManageDao.exportCustomerData(params);
    }

    /** 查询未检测的手机号码 */
    @Override
    public List<String> queryEmptyNumber() {
        emptyNumberList.clear();
        List<String> list = customerManageDao.queryEmptyNumber();
        emptyNumberList.addAll(list);
        return list;
    }

    /** 空号检测 */
    @Override
    public String executeEmptyNumberCheck() {
        if(emptyCheckStatus){
            return "后台正在检测数据中...";
        }
        if(emptyNumberList.size() <= 0){
            emptyNumberList.addAll(queryEmptyNumber());
            if(emptyNumberList.size() <= 0){
                return "暂无未检测号码";
            }
        }
        Map<String, String> emptyNumberConf = propertiesParam.getEmptyNumber();
        if(null == emptyNumberConf || StringUtils.isBlank(emptyNumberConf.get("accesskey"))
                || StringUtils.isBlank(emptyNumberConf.get("secret")) || StringUtils.isBlank(emptyNumberConf.get("url"))){
            return "未配置空号检测账号信息，无法检测";
        }
        // 开启新线程调用api检测手机号码状态
        Thread thread = new Thread(() -> {
            // 创瑞云 接口
//            checkPhoneNumberStatus_cry(emptyNumberConf);
            // 创蓝253接口
            checkPhoneNumberStatus_cl253(emptyNumberConf);
        });
        thread.start();
        return ReturnData.SUCCESS;
    }

    /** 根据手机号码更新数据状态 */
    @Override
    @Transactional
    public int updateByNumber(String phoneNumber, Byte status){
        return customerManageDao.updateByNumber(phoneNumber, status, "");
    }

    // 创瑞云接口
    private void checkPhoneNumberStatus_cry(Map<String, String> emptyNumberConf){
        // 设置检查状态为true
        emptyCheckStatus = true;
        String url = emptyNumberConf.get("url") + "?accesskey={accesskey}&secret={secret}&mobile={mobile}";
        Map<String, String> params = new HashMap<>();
        params.put("accesskey", emptyNumberConf.get("accesskey"));
        params.put("secret", emptyNumberConf.get("secret"));
        while (emptyNumberList.size() > 0){
            String phoneNumber = emptyNumberList.get(emptyNumberList.size() - 1);
            // 移除集合中的号码
            emptyNumberList.remove(phoneNumber);
            if(phoneNumber.length() != 11){
                continue;
            }
            params.put("mobile", phoneNumber);
            JSONObject forObject = null;
            try {
                HttpEntity<String> strEntity = new HttpEntity<>("",new HttpHeaders());
                forObject = restTemplate.postForObject(url,strEntity, JSONObject.class, params);
            } catch (RestClientException e) {
                logger.error("空号检测发生错误：", e);
                break;
            }
            if(null == forObject){
                logger.error(phoneNumber + ":空号检测请求异常，请求返回结果为null");
                break;
            }
            Integer code = forObject.getInteger("code");
            if(200 != code){
                logger.error("空号检测失败：" + forObject.getInteger("message"));
                break;
            }
            Byte state = forObject.getByte("data");
            switch (state){
                case 2:
                    state = 0;
                    break;
                case 3:
                    state = 5;
                    break;
            }
            // 更新数据库状态
            updateByNumber(phoneNumber, state);
        }
        // 空号检查状态设置为false
        emptyCheckStatus = false;
    }

    // 创蓝253 接口
    private void checkPhoneNumberStatus_cl253(Map<String, String> emptyNumberConf){
        String currNumber = "";
        // 设置检查状态为true
        emptyCheckStatus = true;
        String url = emptyNumberConf.get("url") + "?appId={accesskey}&appKey={secret}&mobiles={mobile}";
        Map<String, String> params = new HashMap<>();
        params.put("accesskey", emptyNumberConf.get("accesskey"));
        params.put("secret", emptyNumberConf.get("secret"));
        try {
            while (emptyNumberList.size() > 0){
                String phoneNumber = emptyNumberList.get(emptyNumberList.size() - 1);
                currNumber = phoneNumber;
                // 移除集合中的号码
                emptyNumberList.remove(phoneNumber);
                if(!NumberUtils.isPhoneNun(phoneNumber)){
                    continue;
                }
                params.put("mobile", phoneNumber);
                JSONObject forObject = null;
                try {
                    HttpEntity<String> strEntity = new HttpEntity<>("",new HttpHeaders());
                    forObject = restTemplate.postForObject(url,strEntity, JSONObject.class, params);
                } catch (RestClientException e) {
                    logger.error(phoneNumber + "；空号检测发生错误：", e);
                    break;
                }
                if(null == forObject){
                    logger.error(phoneNumber + "；空号检测请求异常，请求返回结果为null");
                    break;
                }
                Integer code = forObject.getInteger("code");
                if(200000 != code){
                    logger.error( phoneNumber + "；空号检测失败：" + forObject.getInteger("message"));
                    break;
                }
                JSONArray data = forObject.getJSONArray("data");
                JSONObject jsonObject = data.getJSONObject(0);
                Byte status = jsonObject.getByte("status");
                // 更新数据库状态
                updateByNumber(phoneNumber, status);
            }
        } catch (Exception e) {
            // 空号检查状态设置为false
            emptyCheckStatus = false;
            logger.error( currNumber + "；空号检测失败：", e);
        }
        // 空号检查状态设置为false
        emptyCheckStatus = false;
    }
}
