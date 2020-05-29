package com.example.task.sdk;

import com.sunlands.robot.task.DelayTaskConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * redisTemplate key要采用StringRedisSerializer序列化方式，否则会报错
 */
@Component
public class DelayTaskConsumer3 {

    @Autowired
    private DelayTaskConsumer delayTaskConsumer;

    @PostConstruct
    private void start() {
        delayTaskConsumer.start();
    }
}
