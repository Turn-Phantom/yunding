package com.yunding.server.queue.entity;


import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @desc
 * @date 2020-04-08
 */
public class LinkedQueue {
    public static Queue<VisitRecord> linkQueue = new ConcurrentLinkedQueue<>();

    // 默认初始化为当前时间
    public static int currTime;

    // 改变时间，当该值发生改变时，当前时间更改
    public static int changeTime = (int) (System.currentTimeMillis() / 1000);

    // redis 中的key
    public static Queue<String> redisKeys = new ConcurrentLinkedQueue<>();

    // 视频播放记录队列
    public static Queue<String> videoPlayRecord = new ConcurrentLinkedQueue<>();

    // 用户影片播放时长队列
    public static Queue<UserOnlineTime> videoPlayTimeCount = new ConcurrentLinkedQueue<>();
}
