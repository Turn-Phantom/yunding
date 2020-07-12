package com.li.cloud.video.entity;

import java.util.HashMap;
import java.util.Map;

/**
 * @desc 影片类型字段转换
 * @date 2020-04-17
 */
public class VideoClassExchage {

    /** 获取对应影片类别字段转换数据 */
    public static Map<String, String> exchangeData(){
        Map<String, String> exchange = new HashMap<>();
        exchange.put("mianfei", "免費");
        exchange.put("youma", "有碼");
        exchange.put("zhongwen", "中文");
        exchange.put("wmzhongwen", "無碼中文");
        exchange.put("zipai", "自拍");
        exchange.put("duanpian", "免費");
        exchange.put("wuma", "無碼");
        exchange.put("sanji", "三級");
        exchange.put("hanguo", "韓國");
        exchange.put("oumei", "歐美");
        exchange.put("suren", "素人");
        exchange.put("dujia", "獨家");
        exchange.put("donghua", "動畫");
        return exchange;
    }
}
