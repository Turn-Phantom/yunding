package com.li.cloud.common.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.tomcat.util.codec.binary.Base64;

/**
 * @desc 密码工具类
 * @date 2020-03-22
 */
public class PasswordUtil {

    /** 加密 */
    public static String encode(String password) throws Exception {
        // 转字节
        byte[] bytes = password.getBytes("utf-8");
        // 转16进制字符串
        String encodeHexString = Hex.encodeHexString(bytes);
        // 转二进制字节
        byte[] bytes1 = EncryptUtils.parseHexStr2Byte(encodeHexString);
        // 转base64字符串
        String base64String = Base64.encodeBase64String(bytes1);
        // 再次base64加密
        byte[] encodeByte = Base64.encodeBase64(base64String.getBytes("utf-8"));
        return new String(encodeByte);
    }

    /** 解密 */
    public static String decode(String password) throws Exception {
        byte[] bytes = password.getBytes("utf-8");
        // base64解密
        byte[] decodeByte = Base64.decodeBase64(bytes);
        String decodeStr = new String(decodeByte);
        // base64解密为字节
        byte[] bytes1 = Base64.decodeBase64(decodeStr);
        String hexStr = EncryptUtils.parseByteToHexStr(bytes1);
        byte[] bytes2 = Hex.decodeHex(hexStr.toCharArray());
        return new String(bytes2);
    }
}
