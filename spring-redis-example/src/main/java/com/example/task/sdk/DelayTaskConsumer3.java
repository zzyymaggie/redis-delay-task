package com.example.task.sdk;

import com.example.zzyymaggie.task.DelayTaskConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DelayTaskConsumer3 {

    @Autowired
    private DelayTaskConsumer delayTaskConsumer;

    @PostConstruct
    private void start() {
        delayTaskConsumer.start();
    }
}
