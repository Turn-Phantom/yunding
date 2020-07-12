package com.li.cloud.queue.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class LinkedQueueTest {

    @Test
    public void testVideoPlayCount(){
        Queue<UserOnlineTime> videoPlayTimeCount = LinkedQueue.videoPlayTimeCount;
        UserOnlineTime userOnlineTime = new UserOnlineTime();
        userOnlineTime.setUserId(1);
        userOnlineTime.setOnlineSec(20);
        videoPlayTimeCount.offer(userOnlineTime);
    }
}