package com.li.cloud.tool.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.li.cloud.tool.service.CustomerManageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class CustomerManageServiceImplTest {

    RestTemplate restTemplate = new RestTemplate();

    Logger logger = LoggerFactory.getLogger(CustomerManageServiceImplTest.class);

    public static void main(String[] args) {
        new CustomerManageServiceImplTest().checkNumber();
    }

    public void checkNumber(){
        // http://139.196.108.241:8081/Api/Detect.ashx?account=14541591075&pswd=aaa988520.&mobile=13616510022
        String phoneNumber = "18795955929";
        String url = "http://139.196.108.241:8081/Api/Detect.ashx?account={account}&pswd={pswd}&mobile={mobile}";
        Map<String, String> params = new HashMap<>();
        params.put("account", "14541591075");
        params.put("pswd", "aaa988520.");
        params.put("mobile", phoneNumber);
        JSONObject forObject = restTemplate.getForObject(url, JSONObject.class, params);
        if(null == forObject){
            logger.error("空号检测请求异常，请求返回结果为null");
            return;
        }
        Integer code = forObject.getInteger("code");
        if(null == code){
            logger.error("空号检测失败，检测结果状态码为空！");
            return ;
        }
        if(code != 0){
            logger.error("空号检测结果返回错误：" + codeDesc(code));
            return;
        }
        JSONArray jsonArray = forObject.getJSONArray("result");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        Byte state = jsonObject.getByte("state");
        if(state == -1){
            state = 5;
        }
        System.out.println(jsonObject);
    }

    private String codeDesc(Integer code){
        String retVal = "";
        switch (code){
            case -1:
                retVal = "一般错误";
                break;
            case 101:
                retVal = "无此用户";
                break;
            case 102:
                retVal = "密码错误";
                break;
            case 108:
                retVal = "手机号码个数错（>50或<=0）";
                break;
            case 109:
                retVal = "无发送额度（该用户可用条数为0）";
                break;
        }
        return retVal;
    }

}