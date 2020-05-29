package com.example;

import com.example.task.sdk.MessageListenerImpl;
import com.example.zzyymaggie.task.DelayTaskConsumer;
import com.example.zzyymaggie.task.DelayTaskProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisExampleApplication4 {

    public static void main(String [] args){
        SpringApplication.run(RedisExampleApplication4.class,args);
    }

    @Bean
    public DelayTaskConsumer delayTaskConsumer(RedisTemplate<String, String> redisTemplate, MessageListenerImpl messageListenerImpl) {
        DelayTaskConsumer delayTaskConsumer =  new DelayTaskConsumer(redisTemplate);
        delayTaskConsumer.setMessageListener(messageListenerImpl);
        return delayTaskConsumer;
    }

    @Bean
    public DelayTaskProducer delayTaskProducer(RedisTemplate<String, String> redisTemplate) {
        return new DelayTaskProducer(redisTemplate);
    }
}
