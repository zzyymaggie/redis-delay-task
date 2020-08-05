package com.example.zzyymaggie;

import com.example.zzyymaggie.task.DelayTaskConsumer;
import com.example.zzyymaggie.task.DelayTaskProducer;
import com.example.zzyymaggie.listener.MessageListenerImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

/**
 * redis实现延时任务
 * @author zhangyu
 */
@SpringBootApplication
@EnableAsync
public class DelayTaskApplication {

    public static void main(String [] args){
        SpringApplication.run(DelayTaskApplication.class,args);
    }

    @Bean
    public DelayTaskConsumer delayTaskConsumer(RedisTemplate<String, String> redisTemplate, MessageListenerImpl messageListenerImpl) {
        DelayTaskConsumer delayTaskConsumer =  new DelayTaskConsumer(redisTemplate);
        delayTaskConsumer.setLimit(100);
        delayTaskConsumer.setMessageListener(messageListenerImpl);
        return delayTaskConsumer;
    }

    @Bean
    public DelayTaskProducer delayTaskProducer(RedisTemplate<String, String> redisTemplate) {
        return new DelayTaskProducer(redisTemplate);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    AsyncRestTemplate asyncRestTemplate(){
        final SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setTaskExecutor(new SimpleAsyncTaskExecutor());
        requestFactory.setConnectTimeout(3000);
        requestFactory.setReadTimeout(3000);

        final AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        asyncRestTemplate.setAsyncRequestFactory(requestFactory);
        return asyncRestTemplate;
    }
}
