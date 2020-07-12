package com.li.cloud.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.io.IoUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @d1c 解析xml 工具类
 * @date 2020-04-26
 */
public class ParseXmlUtil {

    private static Logger logger = LoggerFactory.getLogger(ParseXmlUtil.class);

    /** 将xml字符串转换map对象(仅适用解析卡洛思短信平台返回的内容) */
    @SuppressWarnings("unchecked")
    public static Map<String, String> toMap(String str){
        SAXReader saxReader = new SAXReader();
        Map<String, String> retMap = new HashMap<>();
        try {
            Document document = saxReader.read(IoUtil.toStream(str, "UTF-8"));
            List<Element> elements = document.getRootElement().elements();
            for (Element element : elements) {
                retMap.put(element.getName(), element.getStringValue());
            }
        } catch (DocumentException e) {
            Map<String, String> retErrMap = new HashMap<>();
            logger.error("转换xml异常", e);
            retErrMap.put("error", "短信发送结果解析转换xml异常");
        }
        return retMap;
    }
}
