package com.li.cloud.third.service.impl;

import com.li.cloud.common.utils.ParseXmlUtil;
import com.li.cloud.third.configuration.params.SmsProviderConf;
import com.li.cloud.third.configuration.pojo.SmsProvider;
import com.li.cloud.third.dao.SmsInterfaceDao;
import com.li.cloud.third.entity.SmsSendRecord;
import com.li.cloud.third.service.SmsInterfaceService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @desc 短信接口服务 逻辑
 * @date 2020-04-26
 */
@Service
public class SmsInterfaceServiceImpl implements SmsInterfaceService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SmsProviderConf smsProvider;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private SmsInterfaceDao smsInterfaceDao;

    /** 查询短信剩余量 */
    @Override
    public Map<String, String> querySmsNum() {
        // 查询请求
        SmsProvider karlos = smsProvider.getKarlos();
        String querySurplus = String.format(karlos.getQuerySurplus(), karlos.getUserId(), karlos.getAccount(), karlos.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(reqParams, headers);
        String response = null;
        try {
            response = restTemplate.postForObject(querySurplus, request, String.class);
        } catch (RestClientException e) {
            try {
                response = restTemplate.postForObject(querySurplus, request, String.class);
            } catch (RestClientException e1) {
                logger.error("短信数据查询异常：", e);
                return MapUtils.EMPTY_MAP;
            }
        }
        Map<String, String> retMap = ParseXmlUtil.toMap(response);
        if(!retMap.isEmpty() && !StringUtils.isEmpty(retMap.get("returnstatus")) && retMap.get("returnstatus").equals("Sucess")){
            // 将查询结果存到redis中
            saveSurplusSms(retMap);
            return retMap;
        } else{
            logger.error("短信数据查询失败：" + retMap);
            return MapUtils.EMPTY_MAP;
        }
    }

    /** 发送短信 */
    @Override
    public Map<String, String> sendSmsCode(String phone, String content) {
        // 检查redis中的短信剩余量; 当剩余数量小于5条，禁止用户继续发送验证码，并发短信通知管理员
        Integer surpure = (Integer) redisTemplate.opsForValue().get(SMS_SURPLUS_NUM);
        if(null != surpure && surpure < 5){
            Map<String, String> retMap = new HashMap<>();
            retMap.put("error", "发送验证码错误，请及时联系客服处理");
            return retMap;
        }
        return sendMessage(phone, content);
    }

    /** 发送短信请求 */
    private Map<String, String> sendMessage(String phone, String content){
        // 查询请求
        SmsProvider karlos = smsProvider.getKarlos();
        String sendUrl = String.format(karlos.getSendUrl(), karlos.getUserId(), karlos.getAccount(), karlos.getPassword(), phone, content);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> reqParams = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(reqParams, headers);
        String response = null;
        try {
            response = restTemplate.postForObject(sendUrl, request, String.class);
        } catch (RestClientException e) {
            try {
                response = restTemplate.postForObject(sendUrl, request, String.class);
            } catch (RestClientException e1) {
                Map<String, String> retMap = new HashMap<>();
                logger.error("短信数据查询异常：", e);
                retMap.put("error", "短信数据查询异常");
                return retMap;
            }
        }
        return ParseXmlUtil.toMap(response);
    }

    /** 保存剩余条数到redis中; 有效时间： 185分钟 */
    private void saveSurplusSms(Map<String, String> retMap){
        Integer surplus = Integer.valueOf(retMap.get("overage"));
        if(surplus < 50){
            // 查询后台配置的管理员联系信息
            Map<String, String> managerContact = smsInterfaceDao.queryManagerContact();
            String p = managerContact.get("phone");
            String msg = managerContact.get("msg");
            if(StringUtils.isEmpty(p) || StringUtils.isEmpty(msg)){
                logger.error("短信数量过低，通知管理员短信发送失败; 参数异常");
                return;
            }
            Map<String, String> sendMessage = sendMessage(p, msg);
            if(!"Success".equals(sendMessage.get("returnstatus"))){
                logger.error("短信数量过低，通知管理员短信发送失败");
            }
        }
        redisTemplate.opsForValue().set(SMS_SURPLUS_NUM, surplus, 185, TimeUnit.MINUTES);
    }

    /** 保存短信发送记录 */
    @Override
    public int saveSendRecord(SmsSendRecord smsSendRecord) {
        return smsInterfaceDao.saveSendRecord(smsSendRecord);
    }
}
