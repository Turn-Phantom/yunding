package com.online.yunding.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * @desc: List集合工具类
 * @date: 2019-06-23
 */
public class ListUtil {

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MathZeroUtil.class);

    /**
     * @desc: 将数组转换为List类型
     * @param: arr 数组
     * @return: List集合
     * @date: 2019-06-23
     */
    public static <T> List<T> ArrToList(T[] arr){
       return Arrays.asList(arr);
    }
}
