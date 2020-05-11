package com.yunding.server.common.utils;

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
        String regex = "^((13[0-9])|(14[579])|(15([0-3,5-9]))|(16[6])|(17[0135678])|(18[0-9]|19[89]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNum);
        return m.matches();
    }
}
