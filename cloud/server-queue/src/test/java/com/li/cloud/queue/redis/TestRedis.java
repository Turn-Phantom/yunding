package com.li.cloud.queue.redis;

import com.li.cloud.queue.entity.VisitRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.Socket;

/**
 * @desc
 * @date 2020-04-11
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Test
    public void getDataByKey(){
        VisitRecord visitRecord = (VisitRecord) redisTemplate.opsForValue().get("192.168.48.47:1586572239");
        redisTemplate.delete("192.168.48.47:1586572239");
        System.out.println(visitRecord);
    }

    @Test
    public void testRedisLink(){
        try {
            Socket socket = new Socket("192.168.48.206", 6379);
            System.out.println(socket.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
