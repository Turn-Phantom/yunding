package com.yunding.server.common.utils;

/**
 * @desc: 字符串操作工具类
 * @date: 2019-06-16
 */
public class StringUtil {

    /**
     * @desc: 删除缓存字符串的最后一个字符
     * @param: sb 缓冲字符串
     * @return: StringBuffer 处理后的缓冲字符串
     * @date: 2019-06-18
     */
    public static StringBuilder delLastChar(StringBuilder sb){
        return sb.deleteCharAt(sb.length() - 1);
    }

    /**
     * @desc: 删除缓存字符串的最后一个字符
     * @param: sb 缓冲字符串
     * @return: StringBuffer 处理后的缓冲字符串
     * @date: 2019-06-18
     */
    public static StringBuffer delLastChar(StringBuffer sb){
        return sb.deleteCharAt(sb.length() - 1);
    }

    /**
     * @desc: 删除字符串中的最后一个字符
     * @param: str 字符串
     * @return: String 处理后的字符串
     * @date: 2019-06-18
     */
    public static String delLastChar(String str){
        return str.substring(0, str.length() - 1);
    }

    /**
     * @desc: 判断字符串是否为空
     * @param: strVal
     * @return: Boolean
     * @date: 2019-09-19
     */
    public static Boolean isEmpty(String strVal){
        return strVal == null || strVal.equals("");
    }

}
