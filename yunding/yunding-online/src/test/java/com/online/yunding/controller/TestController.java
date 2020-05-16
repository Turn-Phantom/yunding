package com.online.yunding.controller;


import org.apache.commons.lang3.StringUtils;

/**
 * @desc
 * @date 2020-03-22
 */
public class TestController {
    public static void main(String[] args) {
//        String md5Hex = DigestUtil.md5Hex("lisheng231218");
//        System.out.println(md5Hex); // 426f70d734ab8681b2343be755cd61ae

        String a = "a123456";
        String sub = StringUtils.substring(a, a.length() - 6);
        System.out.println(sub);
    }

    public static int add(int a, int b){
        return  a + b;
    }

    public static int mult(int a, int b){
        return  a * b;
    }

    public static int sub(int a, int b){
        return  a - b;
    }
}
