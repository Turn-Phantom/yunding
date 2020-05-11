package com.online.yunding.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @desc: 数字计算（转换工具类）
 * @date: 2019-06-22
 */
public class MathZeroUtil {
    private static BigDecimal bigDecimal;

    // 日志
    private static final Logger LOGGER = LoggerFactory.getLogger(MathZeroUtil.class);

    /**
     * @desc: 判断单精度浮点数是否为0
     * @param: f 浮点数类型
     * @return: boolean
     * @date: 2019-06-22
     */
    public static boolean isZero(Float f){
        if(EmptyUtil.isEmpty(f)){
            return true;
        }
        bigDecimal = new BigDecimal(f);
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * @desc: 判断双精度浮点数是否为0
     * @param: d 浮点数类型
     * @return: boolean
     * @date: 2019-06-22
     */
    public static boolean isZero(Double d){
        if(EmptyUtil.isEmpty(d)){
            return true;
        }
        bigDecimal = new BigDecimal(d);
        return bigDecimal.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * @desc: 判断大数据类型是否为0
     * @param: bigD 大数据类型
     * @return: boolean
     * @date: 2019-06-22
     */
    public static boolean isZero(BigDecimal bigD){
        return bigD.compareTo(BigDecimal.ZERO) == 0;
    }
}
