package com.li.cloud.tool.controller;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @desc
 * @date 2020-06-09
 */
public class test {
    public static void main(String[] args) {
        String s = "00861350000211454";
        String replace = StringUtils.replaceOnce(s, "00", "+");
        System.out.println(replace);
    }
}
