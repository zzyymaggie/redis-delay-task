package com.example.task.sdk;

import com.sunlands.robot.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.List;

@Component
public class MessageListenerImpl implements MessageListener {
    @Override
    public void consumeMessage(List<String> ids) {
        for (String id : ids) {
            System.out.println(MessageFormat.format("发布资讯。id - {0} , timeStamp - {1} , " +
                    "threadName - {2}", id, System.currentTimeMillis(), Thread.currentThread().getName()));
        }
    }
}
