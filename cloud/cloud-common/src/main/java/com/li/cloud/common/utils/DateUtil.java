package com.li.cloud.common.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @desc: 日期工具类
 * @date: 2019-06-19
 */
public class DateUtil {

    /**
     * @desc: 根据日期格式化，返回对应的格式化对象
     * @param: pattern 格式
     * @return: SimpleDateFormat 格式化对象
     * @date: 2019-06-19
     */
    private static SimpleDateFormat getDateFormat(String pattern){
        return new SimpleDateFormat(pattern);
    }

    /**
     * @desc: 根据格式，获取对应格式日期字符串
     * @param: date 日期
     * @param: pattern 格式
     * @return: String 格式化后的日期
     * @date: 2019-06-19
     */
    public static String formatDateStr(Date date, String pattern){
        return getDateFormat(pattern).format(date);
    }

    /** java 8 方式 */

    /**
     * @desc: 获取当前日期
     * @param: pattern 日期格式
     * @return: String
     * @date: 2019-09-28
     */
    public static String getCurrentDateStr(String pattern){
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
    }

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
