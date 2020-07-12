package com.li.cloud.video.utils;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.fastjson.JSONObject;
import com.li.cloud.video.entity.VideoList;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @desc 常用工具类
 * @date 2020-04-18
 */
public class VideoServerUtil {

    /** 将json对象的数据结果转换文视频清单对象 */
    public static void setVideoListVal(VideoList videoList, JSONObject jsonObject){
        videoList.setCid(jsonObject.getString("cid"));
        videoList.setState(jsonObject.getInteger("state"));
        videoList.setVideoClass(jsonObject.getString("ch"));
        videoList.setImgQuality1(jsonObject.getInteger("is_1200"));
        videoList.setImgQuality2(jsonObject.getInteger("is_600"));
        videoList.setImgQuality3(jsonObject.getInteger("is_300"));
        videoList.setVideoUrl(jsonObject.getString("v_url"));
        videoList.setPicUrl(jsonObject.getString("p_url"));
        videoList.setVideoName(jsonObject.getString("name"));
        videoList.setActor(jsonObject.getString("actor"));
        videoList.setVideoTag(jsonObject.getString("tag"));
        videoList.setVendor(jsonObject.getString("vendor"));
        videoList.setVideoDesc(jsonObject.getString("desc"));
        videoList.setUpdateTime(jsonObject.getLong("utime"));
        videoList.setClickCount(jsonObject.getInteger("clicks"));
        videoList.setEnableDate(jsonObject.getInteger("enable_date"));
        videoList.setOperateTime(System.currentTimeMillis());
    }

    /** 对比数据与异动的数据； 若对比标识为true； 标识新值与数据库值不一致，需要更新，否则无需更新 */
    public static boolean compareData(VideoList dbVideoList, VideoList videoListData, String[] excludeField){
        boolean compareRes = false;
        Method[] methods = ReflectUtil.getMethods(VideoList.class);
        Map<String, Method> methodMap = Arrays.stream(methods).collect(Collectors.toMap(Method::getName, obj -> obj, (k1, k2) -> k1));
        for (Map.Entry<String, Method> methodEntry : methodMap.entrySet()) {
            String methodName = methodEntry.getKey();
            if(!methodName.startsWith("get") || StringUtils.equalsIgnoreCase("getClass", methodName)){
                continue;
            }
            Method getMethod = methodEntry.getValue();
            String subMethodName = methodName.substring(3);
            // 排除对比字段
            if(excludeField != null){
                boolean isSkip = false;
                for (String field : excludeField) {
                    if(StringUtils.equalsIgnoreCase(field, subMethodName)){
                        isSkip = true;
                    }
                }
                if(isSkip){
                    continue;
                }
            }
            Object[] nullVal = new Object[]{null};
            try {
                Object dbVal = getMethod.invoke(dbVideoList);
                Object newVal = getMethod.invoke(videoListData);
                // 新值不为空且不与数据库相等，设置对象值； 否则置空
                if(null != newVal && !newVal.equals(dbVal)){
                    Method setMethod = methodMap.get("set" + subMethodName);
                    setMethod.invoke(dbVideoList, getMethod.invoke(videoListData));
                    // 对比标识
                    compareRes = true;
                } else{
                    Method setMethod = methodMap.get("set" + subMethodName);
                    setMethod.invoke(dbVideoList, nullVal);
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return compareRes;
    }
}
