package com.yunding.server.common.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @desc: 日期工具类
 * @date: 2019-06-19
 */
public class DateUtil {

    /** 获取当天UTC+0的时间; 返回单位：秒 */
    public static long getUTCTime(){
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        return utc.toEpochSecond();
    }

    /**
     * @desc 获取UTC+0 的格式化时间;
     * @param pattern 格式化
     * @return
     * @date 2020-04-18
     */
    public static String getUTCDate(String pattern){
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        return utc.format(DateTimeFormatter.ofPattern(pattern));
    }

    /** 获取utc前后日期; day: 正数，加天数； 负数，减天数 */
    public static String getUTCDate(String pattern, long day){
        ZonedDateTime utc = ZonedDateTime.now(ZoneOffset.UTC);
        return utc.plusDays(day).format(DateTimeFormatter.ofPattern(pattern));
    }

    /** 获取当前时间 */
    public static String getCurrDate(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }
}
