package com.example.zzyymaggie.task;

import com.example.zzyymaggie.util.Constants;
import com.example.zzyymaggie.listener.MessageListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DelayTaskConsumer {

    private RedisTemplate<String, String> redisTemplate;

    private String queue = Constants.DELAY_TASK_QUEUE;

    private static DefaultRedisScript<List> redisScript;

    private MessageListener messageListener;

    private int offset;

    private int limit = 1;

    private int delayInMillSeconds = 500;

    static  {
        DefaultRedisScript<List> defaultRedisScript = new DefaultRedisScript<>();
        defaultRedisScript.setResultType(List.class);
        defaultRedisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("luascript/delaytask.lua")));
        redisScript = defaultRedisScript;
    }

    public DelayTaskConsumer(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * 需要客户端主动触发调用
     */
    public void start() {
        scheduledExecutorService.scheduleWithFixedDelay(new DelayTaskHandler(), 1, delayInMillSeconds, TimeUnit.MILLISECONDS);
    }

    public class DelayTaskHandler implements Runnable {

        @Override
        public void run() {
            try {
                String[] arguments = new String[4];
                arguments[0] = String.valueOf(0); //起始时间
                arguments[1] = String.valueOf(System.currentTimeMillis()); //结束时间
                arguments[2] = String.valueOf(offset); //offset
                arguments[3] = String.valueOf(limit); //limit
                Object result = redisTemplate.execute(redisScript, Collections.singletonList(queue), arguments);
                if(result != null) {
                    List<String> ids = (List<String>)result;
                    if(ids.size() > 0) {
                        messageListener.consumeMessage(ids);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public void setMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getDelayInMillSeconds() {
        return delayInMillSeconds;
    }

    public void setDelayInMillSeconds(int delayInMillSeconds) {
        this.delayInMillSeconds = delayInMillSeconds;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String getQueue() {
        return queue;
    }

    public void setQueue(String queue) {
        this.queue = queue;
    }
}
