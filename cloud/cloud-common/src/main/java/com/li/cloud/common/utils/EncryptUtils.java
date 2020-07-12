package com.li.cloud.common.utils;

/**
 * @desc 加密工具类
 * @date 2020-03-22
 */
public class EncryptUtils {

    /** 二进制转16进制 */
    public static String parseByteToHexStr(byte buf[]) {
        StringBuilder sb = new StringBuilder();
        for (byte aBuf : buf) {
            String hex = Integer.toHexString(aBuf & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /** 将16进制转换为二进制 */
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
             return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
             int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
             int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
             result[i] = (byte) (high * 16 + low);
         }
        return result;
    }
}