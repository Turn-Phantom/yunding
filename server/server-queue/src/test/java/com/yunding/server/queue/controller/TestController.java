package com.yunding.server.queue.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc
 * @date 2020-04-10
 */
public class TestController {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        BigDecimal b = BigDecimal.ZERO;
        for (Integer integer : list) {
            b = b.add(new BigDecimal(integer));
        }
        System.out.println(b);
    }
}
