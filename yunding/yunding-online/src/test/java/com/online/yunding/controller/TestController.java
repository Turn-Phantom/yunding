package com.online.yunding.controller;

import cn.hutool.crypto.digest.DigestUtil;
import com.online.yunding.common.utils.EncryptUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;

import javax.xml.stream.events.EndDocument;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * @desc
 * @date 2020-03-22
 */
public class TestController {
    public static void main(String[] args) {
//        String md5Hex = DigestUtil.md5Hex("lisheng231218");
//        System.out.println(md5Hex); // 426f70d734ab8681b2343be755cd61ae

        int c = add(12, 10);
        System.out.println(c);
        System.out.println(mult(2, 2));
        System.out.println(sub(2, 2));
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
