package com.online.yunding.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @desc 网络地址工具类
 * @date 2020-04-07
 */
public class InterAddressUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(InterAddressUtils.class);

    public static boolean isEnablePort(String addr, int port){
        try {
            new Socket(addr, port);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    /** 根据http请求获取请求ip */
    public static String getIpForReq(HttpServletRequest request){
        String ip = request.getHeader("X-forwarded-For");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
            try {
                // 判断是否为本机ip
                boolean isLocalIp = InetAddress.getLocalHost().isSiteLocalAddress();
                if(isLocalIp){
                    String reqIp = request.getHeader("reqIp");
                    if(!StringUtils.isBlank(reqIp)){
                        ip = reqIp;
                    }
                }
            } catch (UnknownHostException e) {
                LOGGER.error("获取本机地址失败：", e);
            }
        }
        return ip;
    }

    /** 根据http请求获取浏览器版本 */
    public static String getBrowserVer(HttpServletRequest request){
        String browserVersion = "";
        String header = request.getHeader("user-agent");
        if(!StringUtils.isEmpty(header)){
            if(header.indexOf("MSIE")>0){
                browserVersion = "IE";
            }else if(header.indexOf("Firefox")>0){
                browserVersion = "Firefox";
            }else if(header.indexOf("Chrome")>0){
                browserVersion = "Chrome";
            }else if(header.indexOf("Safari")>0){
                browserVersion = "Safari";
            }else if(header.indexOf("Camino")>0){
                browserVersion = "Camino";
            }else if(header.indexOf("Konqueror")>0){
                browserVersion = "Konqueror";
            }
        }
        return browserVersion;
    }

    /** 获取操作系统版本 */
    public static String getOperateSysVer(HttpServletRequest request){
        String systemInfo = "";
        String header = request.getHeader("user-agent");
        if(header == null || header.equals("")){
            return "";
        }
        //得到用户的操作系统
        if (header.indexOf("NT 6.0") > 0){
            systemInfo = "Windows Vista/Server 2008";
        } else if (header.indexOf("NT 5.2") > 0){
            systemInfo = "Windows Server 2003";
        } else if (header.indexOf("NT 5.1") > 0){
            systemInfo = "Windows XP";
        } else if (header.indexOf("NT 6.0") > 0){
            systemInfo = "Windows Vista";
        } else if (header.indexOf("NT 6.1") > 0){
            systemInfo = "Windows 7";
        } else if (header.indexOf("NT 6.2") > 0){
            systemInfo = "Windows Slate";
        } else if (header.indexOf("NT 6.3") > 0){
            systemInfo = "Windows 9";
        } else if(header.indexOf("NT 10.0") > 0){
            systemInfo = "Windows 10";
        }else if (header.indexOf("NT 5") > 0){
            systemInfo = "Windows 2000";
        } else if (header.indexOf("NT 4") > 0){
            systemInfo = "Windows NT4";
        } else if (header.indexOf("Me") > 0){
            systemInfo = "Windows Me";
        } else if (header.indexOf("98") > 0){
            systemInfo = "Windows 98";
        } else if (header.indexOf("95") > 0){
            systemInfo = "Windows 95";
        } else if (header.indexOf("Mac") > 0){
            systemInfo = "Mac";
        } else if (header.indexOf("Unix") > 0){
            systemInfo = "UNIX";
        } else if (header.indexOf("Linux") > 0){
            systemInfo = "Linux";
        } else if (header.indexOf("SunOS") > 0){
            systemInfo = "SunOS";
        }
        return systemInfo;
    }

    /** 获取mac地址 */
    public static String getMacAddress(String ip){
        String macAddress = "";
        macAddress = getMacInWindows(ip).trim();
        if(StringUtils.isEmpty(macAddress)){
            macAddress = getMacInLinux(ip).trim();
        }
        return macAddress;
    }

    private static String callCmd(String[] cmd) {
        StringBuilder result = new StringBuilder();
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result.append(line);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private static String callCmd(String[] cmd,String[] another) {
        String result = "";
        String line = "";
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(cmd);
            proc.waitFor(); // 已经执行完第一个命令，准备执行第二个命令
            proc = rt.exec(another);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader (is);
            while ((line = br.readLine ()) != null) {
                result += line;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    private static String filterMacAddress(final String ip, final String sourceString,final String macSeparator) {
        String result = "";
        String regExp = "((([0-9,A-F,a-f]{1,2}" + macSeparator + "){1,5})[0-9,A-F,a-f]{1,2})";
        Pattern pattern = Pattern.compile(regExp);
        Matcher matcher = pattern.matcher(sourceString);
        while(matcher.find()){
            result = matcher.group(1);
            if(sourceString.indexOf(ip) <= sourceString.lastIndexOf(matcher.group(1))) {
                break; // 如果有多个IP,只匹配本IP对应的Mac.
            }
        }
        return result;
    }
    private static String getMacInWindows(final String ip){
        String result = "";
        String[] cmd = {"cmd","/c","ping " + ip};
        String[] another = {"cmd","/c","arp -a"};
        String cmdResult = callCmd(cmd,another);
        result = filterMacAddress(ip,cmdResult,"-");
        return result;
    }
    private static String getMacInLinux(final String ip){
        String result = "";
        String[] cmd = {"/bin/sh","-c","ping " +  ip + " -c 2 && arp -a" };
        String cmdResult = callCmd(cmd);
        result = filterMacAddress(ip,cmdResult,":");
        return result;
    }
}
