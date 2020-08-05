package com.example.zzyymaggie.listener;

import com.example.zzyymaggie.service.CallbackHandlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageListenerImpl implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageListenerImpl.class);

    @Autowired
    private CallbackHandlerService callbackHandlerService;

    @Override
    public void consumeMessage(List<String> messageList) {
        for (String message : messageList) {
            LOGGER.debug("consumeMessage:" + message);
            callbackHandlerService.handler(message);
        }
    }
}
