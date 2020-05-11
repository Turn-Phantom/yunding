package com.yunding.server.common.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @desc: 空判断工具类
 * @date: 2019-06-17
 */
public class EmptyUtil {

    /**
     * @desc: 判断char是否为空
     * @param: c char字符
     * @return: boolean
     * @date: 2019-06-22
     */
    public static boolean isEmpty(char c){
        return c == '\0';
    }

    /**
     * @desc: 字符串空判断
     * @param: val 字符串
     * @return: boolean
     * @date: 2019-06-17
     */
    public static boolean isEmpty(String val){
        if(val == null || val.isEmpty()){
            return true;
        }
        return false;
    }

    /**
     * @desc: 对象空判断
     * @param: obj 对象
     * @return: boolean
     * @date: 2019-06-17
     */
    public static <T> boolean isEmpty(T obj){
        if(obj == null || obj.equals("")){
            return true;
        }
        return false;
    }

    /**
     * @desc: 对象空判断
     * @param: obj 对象
     * @return: boolean
     * @date: 2019-06-17
     */
    public static <T> boolean isEmpty(T[] obj){
        if(obj == null || obj.length == 0){
            return true;
        }
        return false;
    }

    /**
     * @desc: Collection集合空判断
     * @param: obj 对象
     * @return: boolean
     * @date: 2019-06-17
     */
    public static boolean isEmpty(Collection list){
        if(isEmpty(list) || list.size() == 0){
            return true;
        }
        return false;
    }

    /**
     * @desc: Map集合空判断
     * @param: obj 对象
     * @return: boolean
     * @date: 2019-06-17
     */
    public static boolean isEmpty(Map map){
        if(isEmpty(map) || map.size() == 0){
            return true;
        }
        return false;
    }

}
