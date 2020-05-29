package com.example.controller;

import com.sunlands.robot.task.DelayTaskProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@RequestMapping("/redis")
@RestController
public class RedisController {
    @Autowired
    private DelayTaskProducer delayTaskProducer;

    @GetMapping("/mock")
    public void mockData() {
        long now = new Date().getTime();
        delayTaskProducer.produce("5", now + TimeUnit.SECONDS.toMillis(5));
        delayTaskProducer.produce("6", now + TimeUnit.SECONDS.toMillis(10));
        delayTaskProducer.produce("7", now + TimeUnit.SECONDS.toMillis(15));
        delayTaskProducer.produce("8", now + TimeUnit.SECONDS.toMillis(20));
    }

}
