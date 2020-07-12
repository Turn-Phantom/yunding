package com.li.cloud.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 数字相关工具类
 * @date 2020-03-31
 */
public class NumberUtils {

    /**
     * @desc 校验是否为手机号码
     * @param phoneNum 手机号码
     * @return boolean
     * @date 2020-03-31
     */
    public static boolean isPhoneNun(String phoneNum){
        if(phoneNum.length() != 11){
            return false;
        }
        String regex = "^(1[3-9]\\d{9}$)";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }
}
