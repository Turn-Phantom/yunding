package com.li.cloud.gateway.sms.sender;

import com.li.cloud.common.basecurd.entity.ReturnData;
import com.li.cloud.gateway.config.params.ServiceIdConfig;
import com.li.cloud.gateway.security.exception.ValidateCodeException;
import com.li.cloud.gateway.security.validate.ValidateCodeSender;
import com.li.cloud.gateway.utils.ConstantUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @desc 自定义验证码发送器
 * @date 2020-04-26
 */
@Component("validateCodeSender")
public class CusValidateCodeSender implements ValidateCodeSender {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 负载均衡的RestTemplate对象
    @Resource(name = "restTemplateForBalance")
    private RestTemplate restTemplate;

    @Autowired
    private ServiceIdConfig serviceIdConfig;

    @Override
    public void send(String mobile, String code, Integer smsType,ServletWebRequest request) {
        /*if(true){
            System.out.println("短信验证码测试数据：");
            System.out.println(mobile);
            System.out.println(code);
            System.out.println(smsType);
            return;
        }*/
        Integer userId;
        try {
            userId = ServletRequestUtils.getIntParameter(request.getRequest(), "userId");
        } catch (ServletRequestBindingException e) {
            logger.error("非法绑定参数：获取用户id失败");
            throw new ValidateCodeException("parse userId illegal");
        }
        String validCodeTypeStr;
        if(smsType == 1){
            validCodeTypeStr = "注册";
        } else if(smsType == 2){
            validCodeTypeStr = "登录";
        } else if(smsType == 3){
            validCodeTypeStr = "换绑";
            if(null == userId){
                throw new ValidateCodeException("userId is null");
            }
        } else if(smsType == 4){
            validCodeTypeStr = "重置密码";
        } else{
            logger.error("非法参数：未知的手机验证码类型" + smsType);
            throw new ValidateCodeException("unKnown sms type");
        }
        String content = String.format(ConstantUtil.SMS_SEND_MSG, validCodeTypeStr, code);

        // 发送验证码
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("mobile", mobile);
        map.add("smsContent", content);
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
        ReturnData returnData = restTemplate.postForObject(serviceIdConfig.getSms() + "/sendSms", httpEntity, ReturnData.class);
        if(null == returnData){
            logger.error("验证码发送失败：请求第三方接口服务返回结果为null");
            throw new ValidateCodeException("sendError");
        }
        if(!ReturnData.SUCCESS.equals(returnData.getReturnType().toString())){
            logger.error("验证码发送失败：" + returnData.getContent());
            throw new ValidateCodeException("sendError");
        }
        Map<String, String> sendRet = (Map)returnData.getObjectData();
        if(null == sendRet || sendRet.isEmpty()){
            logger.error("验证码发送失败，解析验证码发送结果值为空");
            throw new ValidateCodeException("sendError");
        }
        // 发送成功
        if("Success".equals(sendRet.get("returnstatus"))){
            // 保存发送记录到数据库；并记录到redis中
            saveSendRecord(userId, smsType, mobile);
        } else{
            logger.error("验证码发送失败，请求发送失败：" + sendRet);
            throw new ValidateCodeException("sendError");
        }
    }

    /** 保存发送记录到数据库 */
    private void saveSendRecord(Integer userId, Integer smsType, String mobile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        map.add("userId", userId);
        map.add("smsType", smsType);
        map.add("mobile", mobile);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(map, headers);
        ReturnData returnData = restTemplate.postForObject(serviceIdConfig.getSms() + "/saveSendRecord", httpEntity, ReturnData.class);
        if(null == returnData){
            logger.error("保存发送记录到数据库失败， 请求返回结果为空！");
        }
        if(!returnData.getReturnType().toString().equals(ReturnData.SUCCESS)){
            logger.error(returnData.getContent());
        }
    }
}
