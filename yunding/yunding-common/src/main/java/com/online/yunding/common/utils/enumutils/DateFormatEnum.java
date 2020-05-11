package com.online.yunding.common.utils.enumutils;

import lombok.Getter;

/**
 * @desc: 日期格式枚举
 * @date: 2019-06-19
 */
@Getter
public enum DateFormatEnum {
    FORMAT1("yyyyMMdd"),
    FORMAT2("yyyy-MM-dd"),
    FORMAT3("yyyy/MM/dd"),
    FORMAT4("yyyy.MM.dd"),
    FORMAT5("yyyy年MM月dd日"),
    FORMAT6("yyyy-MM-dd HH:mm:ss"),
    FORMAT7("yyyy/MM/dd HH:mm:ss"),
    FORMAT8("yyyy.MM.dd HH:mm:ss"),
    FORMAT9("yyyy年MM月dd日 HH:mm:ss"),
    FORMAT10("yyyy年MM月dd日 HH时mm分ss秒");

    private String format;

    DateFormatEnum(String format) {
        this.format = format;
    }
}
