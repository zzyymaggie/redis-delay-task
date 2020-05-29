package com.example.zzyymaggie.task;

import com.example.zzyymaggie.util.Constants;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.util.List;

public class DelayTaskProducer {

    private RedisTemplate redisTemplate;

    private String queue;

    private static DefaultRedisScript<List> redisScript;

    public DelayTaskProducer(RedisTemplate redisTemplate) {
        this(redisTemplate, Constants.DELAY_TASK_QUEUE);
    }

    public DelayTaskProducer(RedisTemplate redisTemplate, String queue) {
        this.redisTemplate = redisTemplate;
        this.queue = queue;
    }


    public void produce(String newsId, long timeStamp) {
        this.redisTemplate.opsForZSet().add(Constants.DELAY_TASK_QUEUE, newsId, timeStamp);
    }
}
