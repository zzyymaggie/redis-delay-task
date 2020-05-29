package com.example.task.sdk;

import com.example.zzyymaggie.task.DelayTaskProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DelayTaskProducer2 {

    @Autowired
    private DelayTaskProducer delayTaskProducer;

    public void produce(String newsId, long timeStamp) {
        delayTaskProducer.produce(newsId, timeStamp);
    }
}
