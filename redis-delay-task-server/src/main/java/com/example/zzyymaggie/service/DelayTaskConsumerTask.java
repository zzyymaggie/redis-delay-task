package com.example.zzyymaggie.service;

import com.example.zzyymaggie.task.DelayTaskConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 启动consumer任务
 */
@Component
public class DelayTaskConsumerTask {

    @Autowired
    private DelayTaskConsumer delayTaskConsumer;

    @PostConstruct
    private void start() {
        delayTaskConsumer.start();
    }
}
