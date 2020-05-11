package com.yunding.server.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 正则验证相关工具类
 * @date 2020-04-04
 */
public class RegularUtils {

    /**
     * @desc 必须同时包含数字和字母
     * @param validateStr
     * @return
     * @date 2020-04-04
     */
    public static boolean hasNumAndLetter(String validateStr){
        String regex = "^(?![0-9]+$)[a-zA-Z0-9]{6,12}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(validateStr);
        return m.matches();
    }
}
